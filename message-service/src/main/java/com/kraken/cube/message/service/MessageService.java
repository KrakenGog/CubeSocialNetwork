package com.kraken.cube.message.service;

import java.util.List;
import java.util.concurrent.TimeUnit;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kraken.cube.message.dto.CreateMessageRequest;
import com.kraken.cube.message.dto.MessageResponseDto;
import com.kraken.cube.message.entity.Message;
import com.kraken.cube.message.mapper.MessageMapper;
import com.kraken.cube.message.properties.ChatCashProperties;
import com.kraken.cube.message.repository.MessageRepository;
import com.kraken.cube.message.util.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class MessageService {
    
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final SnowflakeIdGenerator snowflakeIdGenerator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ChatCashProperties properties;
    private final SimpMessagingTemplate messagingTemplate;
    



    @Transactional
    public MessageResponseDto createMessage(CreateMessageRequest dto, long authorId) {
        Message message = messageMapper.createRequestDtoToEntity(dto);

        message.setId(snowflakeIdGenerator.nextId());
        message.setAuthorId(authorId);

        Message createdMessage = messageRepository.save(message);

        MessageResponseDto responseDto = messageMapper.entityToMessageResponseDto(createdMessage);

        String key = Long.toString(dto.chatId());

        if(redisTemplate.hasKey(key)){
            redisTemplate.opsForList().rightPush(key, responseDto);
            redisTemplate.opsForList().trim(key, 0, properties.getSize() - 1);
            redisTemplate.expire(key, properties.getLifetime(), TimeUnit.MINUTES);
        }

        String dist = "/topic/chat." + dto.chatId().toString();

        messagingTemplate.convertAndSend(dist, responseDto);

        return responseDto;
    }

    @Transactional
    public List<MessageResponseDto> getAllMessagesFromChat(long chatId) {
        return messageMapper.entityListToMessageResponseDtoList(
                messageRepository.findByChatIdOrderByIdDesc(chatId, PageRequest.of(0, 100)));
    }

    @Transactional
    public List<MessageResponseDto> getTop100MessagesFromChat(long chatId) {
        String key = Long.toString(chatId);
        List<Object> cashed = redisTemplate.opsForList().range(key, 0, -1);

        if (cashed != null && !cashed.isEmpty()) {
            redisTemplate.expire(key, properties.getLifetime(), TimeUnit.MINUTES);
            return cashed
                    .stream()
                    .map(x -> objectMapper.convertValue(x, MessageResponseDto.class))
                    .toList();
        }

        List<MessageResponseDto> messages = messageMapper.entityListToMessageResponseDtoList(
                messageRepository.findByChatIdOrderByIdDesc(chatId, PageRequest.of(0, 100)));

        if(!messages.isEmpty())
            redisTemplate.opsForList().rightPushAll(key, messages.toArray());
            redisTemplate.expire(key, properties.getLifetime(), TimeUnit.MINUTES);

        return messages;
    }
}

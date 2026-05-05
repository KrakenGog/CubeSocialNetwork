package com.kraken.cube.chat.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kraken.cube.chat.entity.Chat;
import com.kraken.cube.chat.entity.ChatParticipant;
import com.kraken.cube.chat.entity.ChatRole;
import com.kraken.cube.chat.repository.ChatParticipantRepository;
import com.kraken.cube.chat.repository.ChatRepository;
import com.kraken.cube.chat.repository.InvintationRepository;
import com.kraken.cube.common.contract.ErrorCode;
import com.kraken.cube.common.dto.UserChatPermissionDto;
import com.kraken.cube.common.exceptionHandling.BuisnessException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final InvintationRepository invintationRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    @Transactional
    public UserChatPermissionDto canUserWriteInChat(Long userId, Long chatId) throws Exception{

        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isEmpty()){
            throw new Exception(String.format("Chat with id %d", chatId));
        }

        boolean res = chatParticipantRepository.existsByUserIdAndChatId(userId, chatId);
        

        if(res)
            return UserChatPermissionDto.builder().canWrite(true).build();
        else
            return UserChatPermissionDto.builder().canWrite(false).errorCode(ErrorCode.USER_BANNED.name()).build();
    }

    @Transactional
    public void enterTheChat(Long chatId, Long userId) throws BuisnessException{

        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isEmpty())
            throw new BuisnessException(ErrorCode.CHAT_DOESNT_EXISTS.name(), HttpStatus.BAD_REQUEST);

        if(chatParticipantRepository.existsByUserIdAndChatId(userId, chatId))
            throw new BuisnessException(ErrorCode.USER_ALREADY_IN_CHAT.name(), HttpStatus.BAD_REQUEST);

        

        if(!chat.get().isOpen() && !invintationRepository.existsByUserIdAndChatId(userId, chatId))
            throw new BuisnessException(ErrorCode.USER_NOT_INVITED.name(), HttpStatus.FORBIDDEN);

        chatParticipantRepository.save(ChatParticipant.builder().chat(chat.get()).chatRole(ChatRole.MEMBER).userId(userId).build());
    }
}

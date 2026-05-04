package com.kraken.cube.chat.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kraken.cube.chat.entity.Chat;
import com.kraken.cube.chat.entity.ChatParticipant;
import com.kraken.cube.chat.repository.ChatRepository;
import com.kraken.cube.common.contract.ErrorCode;
import com.kraken.cube.common.dto.UserChatPermissionDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final ChatRepository chatRepository;

    public UserChatPermissionDto canUserWriteInChat(Long userId, Long chatId) throws Exception{

        Optional<Chat> chat = chatRepository.findById(chatId);

        if(chat.isEmpty()){
            throw new Exception(String.format("Chat with id %d", chatId));
        }

        boolean res = false;

        for(var item : chat.get().getParticipants()){
            if(item.getUserId() == userId)
            {
                res = true;
                break;
            }
        }

        if(res)
            return UserChatPermissionDto.builder().canWrite(true).build();
        else
            return UserChatPermissionDto.builder().canWrite(false).errorCode(ErrorCode.USER_BANNED.name()).build();
    }


}

package com.kraken.cube.chat.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kraken.cube.chat.service.ChatService;
import com.kraken.cube.common.dto.UserChatPermissionDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/canUserWriteInChat")
    public UserChatPermissionDto canUserWriteInChat(@RequestParam Long userId, @RequestParam Long chatId) throws Exception {
        return chatService.canUserWriteInChat(userId, chatId);
    }
    
}

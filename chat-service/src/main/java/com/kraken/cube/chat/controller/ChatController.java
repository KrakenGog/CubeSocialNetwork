package com.kraken.cube.chat.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kraken.cube.chat.service.ChatService;
import com.kraken.cube.common.dto.UserChatPermissionDto;
import com.kraken.cube.common.exceptionHandling.BuisnessException;
import com.kraken.cube.common.filters.SecurityUser;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/canUserWriteInChat")
    public UserChatPermissionDto canUserWriteInChat(@RequestParam Long chatId, Authentication authentication) throws Exception {
        return chatService.canUserWriteInChat(((SecurityUser)authentication.getPrincipal()).getId(), chatId);
    }
    
    @PostMapping("/enterTheChat")
    public ResponseEntity<Void> enterTheChat(@RequestBody Long chatId, Authentication authentication) throws BuisnessException {
        chatService.enterTheChat(chatId, ((SecurityUser)authentication.getPrincipal()).getId());
        
        return ResponseEntity.ok().build();
    }
    
}

package com.kraken.cube.message.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kraken.cube.common.exceptionHandling.BuisnessException;
import com.kraken.cube.common.filters.SecurityUser;
import com.kraken.cube.message.dto.CreateMessageRequest;
import com.kraken.cube.message.dto.MessageDto;
import com.kraken.cube.message.dto.MessageResponseDto;
import com.kraken.cube.message.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/message")
@RequiredArgsConstructor

@Slf4j
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/allMessages")
    public ResponseEntity<List<MessageResponseDto>> getMethodName(@RequestParam(name = "chatId") Long chatId) {
        log.info("All messages");
           return ResponseEntity.ok(messageService.getTop100MessagesFromChat(chatId));
    }
    
    @PostMapping("/send")
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody CreateMessageRequest request) throws BuisnessException
    {
        SecurityUser user = (SecurityUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(messageService.createMessage(request, user.getId()));
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload CreateMessageRequest request, Authentication authentication) throws BuisnessException{
        log.info("Send message from websocket");
        messageService.createMessage(request, ((SecurityUser)authentication.getPrincipal()).getId());
    }
    
}

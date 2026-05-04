package com.kraken.cube.message.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kraken.cube.common.dto.UserChatPermissionDto;
import com.kraken.cube.message.config.FeignConfig;

@FeignClient(name = "chat-service", configuration = FeignConfig.class)
public interface ChatClient {
    @GetMapping("/chat/canUserWriteInChat")
    public UserChatPermissionDto canUserWriteInChat(@RequestParam("userId") Long userId, @RequestParam("chatId") Long chatId);
}

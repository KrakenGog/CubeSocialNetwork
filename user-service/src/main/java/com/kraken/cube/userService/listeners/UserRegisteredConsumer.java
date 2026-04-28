package com.kraken.cube.userService.listeners;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.userService.mapper.UserMapper;
import com.kraken.cube.userService.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredConsumer {
    private final UserService userService;
    private final UserMapper mapper;

    @KafkaListener(topics = "user-topic")
    public void consume(UserRegisteredDto userRegisteredDto){
        log.info("UserRegistered event consumed");
        userService.createUser(mapper.toUserDto(userRegisteredDto));
    }
}

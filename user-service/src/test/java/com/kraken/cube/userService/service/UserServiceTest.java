package com.kraken.cube.userService.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kraken.cube.userService.mapper.UserMapper;
import com.kraken.cube.userService.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repo;
    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void shouldSaveUser(){
        
    }
}

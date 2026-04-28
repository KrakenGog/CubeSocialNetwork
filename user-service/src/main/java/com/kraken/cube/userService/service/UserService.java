package com.kraken.cube.userService.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kraken.cube.userService.dto.UserDto;
import com.kraken.cube.userService.mapper.UserMapper;
import com.kraken.cube.userService.model.User;
import com.kraken.cube.userService.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public List<User> getAllUsers(){
        List<User> res = new ArrayList<>();
        
        for (User iterable_element : userRepository.findAll()) {
            res.add(iterable_element);
        };

        return res;
    }

    
    public void createUser(UserDto userDto){
        userRepository.save(mapper.toUser(userDto));
    }
}

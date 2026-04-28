package com.kraken.cube.userService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.kraken.cube.userService.model.User;
import com.kraken.cube.userService.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/allUsers")
    public List<User> getAllUsers() throws InterruptedException {
        log.info("all users request blyaaaaaaa");
        Thread.sleep(1000);
        return service.getAllUsers();
    }
    
}

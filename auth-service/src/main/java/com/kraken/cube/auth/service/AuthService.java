package com.kraken.cube.auth.service;

import java.util.Optional;
import java.util.Set;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import com.kraken.cube.auth.entity.UserAuth;
import com.kraken.cube.auth.errors.RegistrationException;
import com.kraken.cube.auth.repository.RoleRepository;
import com.kraken.cube.auth.repository.UserAuthRepository;

import com.kraken.cube.common.dto.UserRegisteredDto;
import com.kraken.cube.outboxmessage.OutboxSender;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserAuthRepository userAuthRepository;
    
    private final RoleRepository roleRepository;
    private final OutboxSender sender;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(
            String username,
            String name,
            String surname,
            String patronymic,
            String phone,
            String password) throws Exception {

        Optional<UserAuth> auth = userAuthRepository.findByUsername(username);

        if (!auth.isEmpty())
            throw new RegistrationException("User already exists");
        
        
        UserAuth userAuth = UserAuth.builder()
                .roles(Set.of(roleRepository.getUserRole()))
                .username(username)
                .passwordHash(passwordEncoder.encode(password))
                .build();

        userAuthRepository.save(userAuth);

        log.info("Saved");
        sender.send("user-topic", userAuth.getId().toString() ,UserRegisteredDto.builder()
                .username(username)
                .patronymic(patronymic)
                .phone(phone)
                .name(name)
                .surname(surname)
                .authId(userAuth.getId())
                .build());

        
    }

}

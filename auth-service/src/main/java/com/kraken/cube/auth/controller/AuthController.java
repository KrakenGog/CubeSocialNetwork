package com.kraken.cube.auth.controller;

import org.springframework.web.bind.annotation.RestController;

import com.kraken.cube.auth.dto.LoginRequestDto;
import com.kraken.cube.auth.dto.RegisterRequestDto;

import com.kraken.cube.auth.dto.TokenResponseDto;
import com.kraken.cube.auth.entity.UserAuth;
import com.kraken.cube.auth.repository.UserAuthRepository;
import com.kraken.cube.auth.service.AuthService;
import com.kraken.cube.auth.util.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final UserAuthRepository userAuthRepository;
        private final JwtUtil jwtUtil;
        private final AuthService authService;

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
                log.info(String.format("Request to login with name %s and password %s", request.getUsername(),
                                request.getPassword()));

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                log.info("Successful login, generating token as responce");

                UserAuth auth = userAuthRepository.findByUsername(request.getUsername()).get();
                String token = jwtUtil.generateToken(auth);

                log.info("Successful token generation");

                return ResponseEntity.ok(new TokenResponseDto(token));
        }

        @PostMapping("/register")
        public ResponseEntity<Void> register(
                        @RequestBody RegisterRequestDto regDto) throws Exception {
                log.info("user register");

                authService.register(
                                regDto.getUsername(),
                                regDto.getName(),
                                regDto.getSurname(),
                                regDto.getPatronymic(),
                                regDto.getPhone(),
                                regDto.getPassword());

                return ResponseEntity.status(HttpStatus.CREATED).build();
        }

}

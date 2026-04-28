package com.kraken.cube.auth.service;

import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kraken.cube.auth.entity.Role;
import com.kraken.cube.auth.repository.UserAuthRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAuthRepository userAuthRepository;


    public CustomUserDetailsService(UserAuthRepository userAuthRepository) {
        this.userAuthRepository = userAuthRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userAuthRepository.findByUsername(username);

        if(!user.isPresent())
            throw new UsernameNotFoundException(username);

        return User.withUsername(username)
                   .password(user.get().getPasswordHash())
                   .authorities(user.get().getRoles().stream()
                                    .map(Role::getName)
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList()))
                    .build();
    }
    
}

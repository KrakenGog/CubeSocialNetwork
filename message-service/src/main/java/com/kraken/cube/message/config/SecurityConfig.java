package com.kraken.cube.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.kraken.cube.common.exceptionHandling.AccessDeniedEntryPoint;
import com.kraken.cube.common.exceptionHandling.AuthEntryPoint;
import com.kraken.cube.common.filters.CustomSecurityHeadersFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            CustomSecurityHeadersFilter filter,
            @Lazy AccessDeniedEntryPoint accessDeniedEntryPoint,
            @Lazy AuthEntryPoint authEntryPoint) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(
                        e -> e.accessDeniedHandler(accessDeniedEntryPoint).authenticationEntryPoint(authEntryPoint))
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/ws/**").permitAll() 
                        .requestMatchers("/message/**").hasRole("USER")
                        .anyRequest().authenticated())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
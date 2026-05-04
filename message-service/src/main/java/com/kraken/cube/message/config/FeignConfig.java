package com.kraken.cube.message.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor(){
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = attributes.getRequest();

            template.header("X-User-Id", request.getHeader("X-User-Id"));
            template.header("X-User-Roles", request.getHeader("X-User-Roles"));

        };
    }
}

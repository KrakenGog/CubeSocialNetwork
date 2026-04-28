package com.kraken.cube.common.exceptionHandling;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private final GatewayExceptionHandler globalExceptionHandler;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        return globalExceptionHandler.handle(exchange, ex);
    }
    
}

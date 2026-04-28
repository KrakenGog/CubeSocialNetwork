package com.kraken.cube.common.exceptionHandling;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveAccessDeniedHandler implements ServerAccessDeniedHandler {

    private final GatewayExceptionHandler globalExceptionHandler;
    
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return globalExceptionHandler.handle(exchange, denied);
    }
    
}

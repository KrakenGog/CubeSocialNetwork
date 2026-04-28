package com.kraken.cube.gateway.config;

import com.kraken.cube.common.exceptionHandling.ExceptionAutoConfiguration;
import com.kraken.cube.common.exceptionHandling.ReactiveAccessDeniedHandler;
import com.kraken.cube.common.exceptionHandling.ReactiveAuthEntryPoint;
import com.kraken.cube.gateway.filter.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    @Lazy
    private final ReactiveAuthEntryPoint authEntryPoint;
    @Lazy
    private final ReactiveAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter,
            @Lazy ReactiveAuthEntryPoint authEntryPoint,
            @Lazy ReactiveAccessDeniedHandler accessDeniedHandler) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.authEntryPoint = authEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            
            
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/ws/**").permitAll() 
                .pathMatchers("/api/auth/**").permitAll() // pathMatchers вместо requestMatchers
                .pathMatchers("/api/user/**").hasRole("ADMIN")
                .anyExchange().authenticated()
            )
            
            
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            
            
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
            )
            
            
            .addFilterAt(jwtTokenFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            
            .build();
    }
}
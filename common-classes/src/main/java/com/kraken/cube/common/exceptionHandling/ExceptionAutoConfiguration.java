package com.kraken.cube.common.exceptionHandling;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.kraken.cube.common.filters.CustomSecurityHeadersFilter;
import com.kraken.cube.common.util.JwtUtil;

@AutoConfiguration
public class ExceptionAutoConfiguration {
    @ConditionalOnWebApplication(type = Type.SERVLET)
    @AutoConfiguration
    public static class MvcExceptionConfig {
        @Bean
        public GlobalExceptionHandler globalExceptionHandler() {
            return new GlobalExceptionHandler();
        }

        @Bean
        public AuthEntryPoint authEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
            return new AuthEntryPoint(handlerExceptionResolver);
        }

        @Bean
        public AccessDeniedEntryPoint accessDeniedEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
            return new AccessDeniedEntryPoint(handlerExceptionResolver);
        }

        @Bean
        public FilterRegistrationBean<CustomSecurityHeadersFilter> filterRegistrationBean(CustomSecurityHeadersFilter filter){
            FilterRegistrationBean<CustomSecurityHeadersFilter> registration = new FilterRegistrationBean<>(filter);

            registration.setEnabled(false);

            return registration;
        }

        @Bean
        public CustomSecurityHeadersFilter customSecurityHeadersFilter(){
            return new CustomSecurityHeadersFilter();
        }

        @Bean
        public JwtUtil jwtUtil(){
            return new JwtUtil();
        }
    }

    @ConditionalOnWebApplication(type = Type.REACTIVE)
    @AutoConfiguration
    public static class WebFluxExceptionConfig {
        @Bean
        @Order(-1) // Нужно установить приоритет выше (меньше число), чем у
                   // DefaultErrorWebExceptionHandler
        public GatewayExceptionHandler gatewayExceptionHandler(
                ErrorAttributes errorAttributes,
                WebProperties webProperties, // Инжектим стандартные свойства
                ApplicationContext applicationContext,
                ServerCodecConfigurer serverCodecConfigurer) {

            GatewayExceptionHandler handler = new GatewayExceptionHandler(
                    errorAttributes,
                    webProperties,
                    applicationContext,
                    serverCodecConfigurer);

            
            handler.setMessageWriters(serverCodecConfigurer.getWriters());
            handler.setMessageReaders(serverCodecConfigurer.getReaders());

            return handler;
        }

        @Bean
        @Order(-1)
        public ReactiveAuthEntryPoint reactiveAuthEntryPoint(GatewayExceptionHandler gatewayExceptionHandler) {
            return new ReactiveAuthEntryPoint(gatewayExceptionHandler);
        }

        @Bean
        @Order(-1)
        public ReactiveAccessDeniedHandler reactiveAccessDeniedEntryPoint(GatewayExceptionHandler gatewayExceptionHandler) {
            return new ReactiveAccessDeniedHandler(gatewayExceptionHandler);
        }
    }

}

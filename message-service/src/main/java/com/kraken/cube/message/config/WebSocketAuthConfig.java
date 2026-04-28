package com.kraken.cube.message.config;

import com.kraken.cube.common.filters.SecurityUser; // Ваш класс пользователя
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // Выполнять этот конфиг одним из первых
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {
    
    // @Autowired
    // private JwtTokenProvider jwtTokenProvider; // Внедрите ваш сервис для валидации JWT

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Ищем фрейм CONNECT
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    
                    // Достаем заголовок 'Authorization' из STOMP-сообщения
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String jwt = authHeader.substring(7);
                        
                        
                        
                        // ---- ЗАГЛУШКА ДЛЯ ТЕСТА ----
                        // Предположим, токен валиден, и мы создаем пользователя вручную
                        SecurityUser user = new SecurityUser();
                        user.setId(123L); // Id из токена
                        // user.setRole(...)
                        
                        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        accessor.setUser(authentication);
                        // -----------------------------

                    } else {
                         // Можно выбросить исключение, чтобы прервать соединение, если токена нет
                         throw new IllegalArgumentException("No token found");
                    }
                }
                return message;
            }
        });
    }
}
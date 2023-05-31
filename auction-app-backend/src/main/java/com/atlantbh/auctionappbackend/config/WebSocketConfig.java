package com.atlantbh.auctionappbackend.config;

import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenService tokenService;

    private final AppUserRepository appUserRepository;

    public static Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Value("${allowedOrigin}")
    private String allowedOrigin;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableStompBrokerRelay("/exchange", "/queue", "/topic")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/notifications")
                .setAllowedOrigins(allowedOrigin)
                .withSockJS()
                .setInterceptors(handshakeInterceptor());
    }

    @Bean
    public HandshakeInterceptor handshakeInterceptor() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                           WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                String jwt = tokenService.getJwtFromCookie(request);
                log.debug("JWT: " + jwt);
                if(StringUtils.hasText(jwt) && tokenService.validateToken(jwt)) {
                    String email = tokenService.getClaimFromToken(jwt, "sub");
                    log.debug("Email: " + email);
                    Optional<AppUser> user = appUserRepository.getByEmail(email);
                    if(user.isPresent()) {
                        log.debug("UserId: " + user.get().getId());
                        attributes.put("id", user.get().getId());
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Exception exception) {
                log.debug("After handshake");
            }
        };
    }
}


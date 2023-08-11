package com.atlantbh.auctionappbackend.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_WATCHERS_KEY;

@Controller
@AllArgsConstructor
@Api("Users watching auction Controller")
public class WatchersController {

    private final StringRedisTemplate template;

    private SimpMessagingTemplate simpMessagingTemplate;

    private static final Logger log = LoggerFactory.getLogger(WatchersController.class);


    @MessageMapping("/watching/start")
    public void startWatching(@Payload Map<String, String> body, @Header("userId") String userId) {
        String productId = body.get("productId");
        log.debug("Product id fro tracking: {}", productId);
        template.opsForSet().add(AUCTION_WATCHERS_KEY + productId, userId);
    }

    @MessageMapping("/watching/stop")
    public void stopWatching(@Payload Map<String, String> body, @Header("userId") String userId) {
        String productId = body.get("productId");
        log.debug("Removing user id from product auction: {}", userId);
        log.debug("Targeting the product id for user id removal: {}", productId);
        template.opsForSet().remove(AUCTION_WATCHERS_KEY + productId, userId);
    }

    @MessageMapping("/watching/count")
    public void watchingCount(@Header("productId") Long productId) {
        Long count = template.opsForSet().size(AUCTION_WATCHERS_KEY + productId);
        log.debug("Count: {}", count);
        simpMessagingTemplate.convertAndSend("/topic/watching/count", count);
    }

}

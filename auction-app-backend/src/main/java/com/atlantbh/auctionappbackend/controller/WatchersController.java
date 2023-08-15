package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.service.WatchersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
@Api("Controller for tracking auction watchers")
public class WatchersController {

    private final StringRedisTemplate template;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final WatchersService watchersService;

    @MessageMapping("/watching/start")
    @ApiOperation(value = "Start watching a product", notes = "Adds the user on set of watchers for the auction on which the user lands on")
    public void startWatching(@Payload Map<String, String> body, @Header("userId") String userId) {
        String productId = body.get("productId");
        watchersService.addWatchers(productId, userId);
        Long size = template.opsForSet().size(AUCTION_WATCHERS_KEY + productId);
        if (size != null) {
            simpMessagingTemplate.convertAndSend("/topic/watching/count/", size);
        }
    }

    @MessageMapping("/watching/stop")
    @ApiOperation(value = "Stop watching a product", notes = "Removes the user from set of watchers upon the user leaving the product auction page")
    public void stopWatching(@Payload Map<String, String> body, @Header("userId") String userId) {
        String productId = body.get("productId");
        watchersService.removeWatchers(productId, userId);
        Long watchersSize = watchersService.getWatcherCount(productId);
        if (watchersSize != null) {
            simpMessagingTemplate.convertAndSend("/topic/watching/count", watchersSize);
        }
    }

    @MessageMapping("/watching/count")
    @ApiOperation(value = "Returns the number of users watching in real time", notes = "Provides the information about how many users are currently on auction to cause urgency")
    public void watchingCount(@Header("productId") String productId) {
        Long watchersSize = watchersService.getWatcherCount(productId);
        if (watchersSize != null) {
            simpMessagingTemplate.convertAndSend("/topic/watching/count", watchersSize);

        }
    }
}

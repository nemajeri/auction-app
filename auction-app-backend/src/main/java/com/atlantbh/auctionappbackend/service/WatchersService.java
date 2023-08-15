package com.atlantbh.auctionappbackend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_WATCHERS_KEY;

@Service
@AllArgsConstructor
public class WatchersService {

    private final StringRedisTemplate template;

    public void addWatchers(String productId, String userId) {
        template.opsForSet().add(AUCTION_WATCHERS_KEY + productId, userId);
    }

    public Long removeWatchers(String productId, String userId) {
        return template.opsForSet().remove(AUCTION_WATCHERS_KEY + productId, userId);
    }

    public Long getWatcherCount(String productId) {
        return template.opsForSet().size(AUCTION_WATCHERS_KEY + productId);
    }
}

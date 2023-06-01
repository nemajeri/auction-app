package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.request.UserMaxBidRequest;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_EXCHANGE;
import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_ROUTING_KEY;

@Service
@AllArgsConstructor
public class NotificationService {

    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final BidRepository bidRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 3600000 * 24, initialDelay = 36000 * 12)
    @Transactional
    public void checkAuctions() {

        Stream<Product> products = productRepository.findEndedAndUnsoldProducts(ZonedDateTime.now());
        PageRequest pageable = PageRequest.of(0, 1);

        products.forEach(product -> {
            List<UserMaxBidRequest> userWithHighestBid = bidRepository.findHighestBidAndUserByProduct(product.getId(), pageable);

            if (!userWithHighestBid.isEmpty()) {
                AppUser highestBidder = appUserRepository.getById(userWithHighestBid.get(0).getUserId());
                UUID uuid = UUID.randomUUID();

                try {
                    NotificationResponse response = NotificationResponse.builder()
                            .id(uuid)
                            .date(ZonedDateTime.now())
                            .userId(highestBidder.getId())
                            .productId(product.getId())
                            .description("Auction has finished. You are the highest bidder for product: " + product.getProductName())
                            .type(NotificationType.AUCTION_FINISHED)
                            .build();
                    rabbitTemplate.convertAndSend(AUCTION_FINISHED_EXCHANGE, AUCTION_FINISHED_ROUTING_KEY, response);
                } catch (Exception e) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw e;
                }
            }
        });
    }

}

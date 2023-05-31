package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.exception.NotificationNotFound;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Notification;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.NotificationRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.request.UserMaxBidRequest;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_EXCHANGE;
import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_ROUTING_KEY;

@Service
@AllArgsConstructor
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final ProductRepository productRepository;
    private final NotificationRepository notificationRepository;
    private final AppUserRepository appUserRepository;
    private final BidRepository bidRepository;
    private final RabbitTemplate rabbitTemplate;

    @Scheduled( fixedDelay = 3600000 * 24, initialDelay = 36000 * 12)
    @Transactional
    public void checkAuctions() {

        Stream<Product> products = productRepository.findEndedAndUnsoldProducts(ZonedDateTime.now());
        PageRequest pageable = PageRequest.of(0, 1);

        products.forEach(product -> {
            List<UserMaxBidRequest> userWithHighestBid = bidRepository.findHighestBidAndUserByProduct(product.getId(), pageable);

            if (!userWithHighestBid.isEmpty()) {
                AppUser highestBidder = appUserRepository.getById(userWithHighestBid.get(0).getUserId());

                String notificationKey = generateNotificationKey(highestBidder.getId(), product.getId(), NotificationType.AUCTION_FINISHED);

                Notification notification = new Notification();
                notification.setUser(highestBidder);
                notification.setProduct(product);
                notification.setType(NotificationType.AUCTION_FINISHED);
                notification.setUniqueAuctionKey(notificationKey);

                boolean hasBeenSentBefore = notificationRepository.existsByUniqueAuctionKey(notificationKey);

                if(hasBeenSentBefore) {
                    return;
                }

                try {
                    Notification savedNotification = notificationRepository.save(notification);
                    NotificationResponse response = NotificationResponse.builder()
                            .id(savedNotification.getId())
                            .date(ZonedDateTime.now())
                            .userId(highestBidder.getId())
                            .productId(product.getId())
                            .description(savedNotification.getDescription())
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


    public List<NotificationResponse> getNotificationsForUser(Long userId) {

        List<Notification> notifications = notificationRepository.findPendingNotifications(userId);

        return notifications.stream().map(notification -> {
            NotificationResponse response = NotificationResponse.builder()
                    .id(notification.getId())
                    .date(notification.getDate())
                    .userId(notification.getUser().getId())
                    .productId(notification.getProduct().getId())
                    .description(notification.getDescription())
                    .type(notification.getType())
                    .isSentToClient(true)
                    .build();

            notification.setIsDelivered(true);
            notification.setIsSentToClient(true);
            notificationRepository.save(notification);

            return response;
        }).toList();
    }


    public void removeNotification(String notificationId) {
        try {
            Long id = Long.parseLong(notificationId);
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to remove notification", e);
            throw new NotificationNotFound("Notification not found!");
        }
    }

    private String generateNotificationKey(Long userId, Long productId, NotificationType type) {
        return userId + "-" + productId + "-" + type.toString();
    }

}

package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Optional;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_QUEUE;
import static com.atlantbh.auctionappbackend.utils.Constants.OUTBID_QUEUE;

@Controller
@AllArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final SimpMessagingTemplate template;

    private final AppUserRepository appUserRepository;

    @RabbitListener(queues = AUCTION_FINISHED_QUEUE)
    public void receiveAuctionFinishedNotification(NotificationResponse notification) {
        Optional<AppUser> userOpt = appUserRepository.findById(notification.getUserId());
        if (userOpt.isPresent()) {
            this.template.convertAndSend(
                    "/queue/notifications-" + userOpt.get().getId(),
                    notification
            );
        } else {
            log.debug("No user found for id " + notification.getUserId() + ", skipping...");
        }
    }

    @RabbitListener(queues = OUTBID_QUEUE)
    public void receiveOutbidNotification(NotificationResponse notification) {
        Optional<AppUser> userOpt = appUserRepository.findById(notification.getUserId());
        if (userOpt.isPresent()) {
            this.template.convertAndSend(
                    "/queue/notifications-" + userOpt.get().getId(),
                    notification
            );
        } else {
            log.debug("No user found for id " + notification.getUserId() + ", skipping...");
        }
    }
}

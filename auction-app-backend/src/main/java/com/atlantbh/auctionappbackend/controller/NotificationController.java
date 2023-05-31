package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Notification;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.NotificationRepository;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import com.atlantbh.auctionappbackend.service.NotificationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.atlantbh.auctionappbackend.utils.Constants.AUCTION_FINISHED_QUEUE;
import static com.atlantbh.auctionappbackend.utils.Constants.OUTBID_QUEUE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@Controller
@AllArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    private final SimpMessagingTemplate template;
    private final AppUserRepository appUserRepository;
    private final NotificationRepository notificationRepository;

    @GetMapping("/api/v1/get-notifications")
    public ResponseEntity<List<NotificationResponse>> getNotifications(@RequestParam("userId") Long userId) {
        try {
            List<NotificationResponse> notifications = notificationService.getNotificationsForUser(userId);
            if (notifications.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), OK);
            }
            return new ResponseEntity<>(notifications, OK);
        } catch (Exception e) {
            log.error("Exception while getting notifications: ", e);
            return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
        }
    }


    @RabbitListener(queues = AUCTION_FINISHED_QUEUE)
    public void receiveAuctionFinishedNotification(NotificationResponse notification) {
        Optional<AppUser> userOpt = appUserRepository.findById(notification.getUserId());
        log.debug("Notification user ID: " + notification.getUserId());
        if (userOpt.isPresent()) {
            Optional<Notification> notificationOpt = notificationRepository.findById(notification.getId());
            log.debug("Notification ID: " + notification.getId());
            if (notificationOpt.isPresent()) {
                this.template.convertAndSend(
                        "/queue/notifications-" + userOpt.get().getId(),
                        notification
                );

                Notification actualNotification = notificationOpt.get();
                actualNotification.setIsSentToClient(true);
                actualNotification.setIsDelivered(true);
                notificationRepository.save(actualNotification);
            } else {
                log.debug("Notification with id " + notification.getId() + " has already been sent to the client, skipping...");
            }
        }
    }

    @RabbitListener(queues = OUTBID_QUEUE)
    public void receiveOutbidNotification(NotificationResponse notification) {
        Optional<AppUser> userOpt = appUserRepository.findById(notification.getUserId());
        if (userOpt.isPresent()) {
            Optional<Notification> notificationOpt = notificationRepository.findById(notification.getId());
            if (notificationOpt.isPresent()) {
                this.template.convertAndSend(
                        "/queue/notifications-" + userOpt.get().getId(),
                        notification
                );

                Notification actualNotification = notificationOpt.get();
                actualNotification.setIsSentToClient(true);
                actualNotification.setIsDelivered(true);
                notificationRepository.save(actualNotification);
            } else {
                log.debug("Notification with id " + notification.getId() + " has already been sent to the client, skipping...");
            }
        }
    }


    @MessageMapping("/remove-notification")
    public void removeNotification(Principal principal, String notificationId) {
        String email = principal.getName();
        Optional<AppUser> userOpt = appUserRepository.getByEmail(email);
        if (userOpt.isPresent()) {
            notificationService.removeNotification(notificationId);

        }
    }
}


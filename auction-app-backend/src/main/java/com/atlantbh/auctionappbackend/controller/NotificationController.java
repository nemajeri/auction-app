package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.Notification;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate template;


    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public Notification send(Notification notification) {
        this.template.convertAndSend("/topic/notifications", notification);
        return notification;
    }
}


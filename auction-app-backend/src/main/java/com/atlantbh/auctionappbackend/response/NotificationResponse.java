package com.atlantbh.auctionappbackend.response;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Product;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder
@Data
public class NotificationResponse {

    private ZonedDateTime date;

    private NotificationType type;

    private String description;

    private Product product;

    private AppUser user;

    private boolean read;
}

package com.atlantbh.auctionappbackend.response;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

@Builder
@Data
public class NotificationResponse implements Serializable {

    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private ZonedDateTime date;

    private NotificationType type;

    private String description;

    private Long productId;

    private Long userId;
}

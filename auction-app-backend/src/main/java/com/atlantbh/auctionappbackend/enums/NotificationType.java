package com.atlantbh.auctionappbackend.enums;

public enum NotificationType {
    OUTBID("You have been outbid on product: %s"),
    AUCTION_FINISHED("Auction has finished. You are the highest bidder for product: %s");

    private final String messageTemplate;

    NotificationType(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }
}
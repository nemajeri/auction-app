package com.atlantbh.auctionappbackend.model;

import com.atlantbh.auctionappbackend.enums.NotificationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "notification", schema = "auction_app_schema")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private ZonedDateTime date;

    @NotNull
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "sent_to_client", nullable = false)
    private Boolean isSentToClient = false;

    @Column(name = "is_delivered", nullable = false)
    private Boolean isDelivered = false;

    @Version
    private Integer version;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "unique_auction_key", unique = true)
    private String uniqueAuctionKey;

    public String getDescription() {
        return String.format(type.getMessageTemplate(), product.getProductName());
    }
}


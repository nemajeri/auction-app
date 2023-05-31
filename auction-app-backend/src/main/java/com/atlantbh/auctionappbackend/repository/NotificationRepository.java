package com.atlantbh.auctionappbackend.repository;

import com.atlantbh.auctionappbackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    void deleteById(Long notificationId);

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId AND ((n.isSentToClient = false AND n.isDelivered = false AND n.isRead = false) " +
            "OR (n.isSentToClient = true AND n.isDelivered = true AND n.isRead = false))")
    List<Notification> findPendingNotifications(Long userId);

    boolean existsByUniqueAuctionKey(String uniqueAuctionKey);
}

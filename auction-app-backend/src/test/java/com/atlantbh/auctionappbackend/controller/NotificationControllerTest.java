package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {

    @Mock
    private SimpMessagingTemplate template;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    public void receiveAuctionFinishedNotification_ShouldSendNotification_WhenUserExists() {
        NotificationResponse notification = NotificationResponse.builder()
                .userId(1L)
                .build();
        notification.setUserId(1L);
        AppUser user = new AppUser();
        user.setId(1L);

        when(appUserRepository.findById(notification.getUserId())).thenReturn(Optional.of(user));

        notificationController.receiveAuctionFinishedNotification(notification);

        verify(template, times(1)).convertAndSend("/queue/notifications-" + user.getId(), notification);
    }

    @Test
    public void receiveAuctionFinishedNotification_ShouldNotSendNotification_WhenUserDoesNotExist() {

        NotificationResponse notification = NotificationResponse.builder()
                .userId(1L)
                .build();
        notification.setUserId(1L);

        when(appUserRepository.findById(notification.getUserId())).thenReturn(Optional.empty());

        notificationController.receiveAuctionFinishedNotification(notification);

        verify(template, never()).convertAndSend(anyString(), any(NotificationResponse.class));
    }

    @Test
    public void receiveOutbidNotification_ShouldSendNotification_WhenUserExists() {
        NotificationResponse notification = NotificationResponse.builder()
                .userId(1L)
                .build();
        notification.setUserId(1L);
        AppUser user = new AppUser();
        user.setId(1L);

        when(appUserRepository.findById(notification.getUserId())).thenReturn(Optional.of(user));

        notificationController.receiveOutbidNotification(notification);

        verify(template, times(1)).convertAndSend("/queue/notifications-" + user.getId(), notification);
    }

    @Test
    public void receiveOutbidNotification_ShouldNotSendNotification_WhenUserDoesNotExist() {

        NotificationResponse notification = NotificationResponse.builder()
                .userId(1L)
                .build();
        notification.setUserId(1L);

        when(appUserRepository.findById(notification.getUserId())).thenReturn(Optional.empty());

        notificationController.receiveOutbidNotification(notification);

        verify(template, never()).convertAndSend(anyString(), any(NotificationResponse.class));

    }
}

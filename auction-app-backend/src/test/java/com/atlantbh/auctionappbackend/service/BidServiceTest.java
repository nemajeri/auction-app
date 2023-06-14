package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.exception.BidAmountException;
import com.atlantbh.auctionappbackend.exception.ProductNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.dto.UserMaxBidRecord;
import com.atlantbh.auctionappbackend.response.NotificationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;


import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
public class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private HttpServletRequest request;

    @Mock
    private TokenService tokenService;

    @Test
    @DisplayName("Should create a new bid")
    @WithMockUser(username = "test@test.com")
    void createBid_ShouldSucceed_ShouldCreateBidWhenAllParametersAreValid() throws ProductNotFoundException, BidAmountException, AppUserNotFoundException {

        Long productId = 1L;
        Long appUserId = 5L;
        Long productOwnerId = 6L;
        Long highestBidUserId = 7L;
        float amount = 100.0f;

        AppUser appUser = new AppUser();
        appUser.setId(appUserId);

        AppUser productOwner = new AppUser();
        productOwner.setId(productOwnerId);

        Product product = new Product();
        product.setHighestBid(50.0f);
        product.setStartDate(ZonedDateTime.now().minusDays(1));
        product.setEndDate(ZonedDateTime.now().plusDays(1));
        product.setUser(productOwner);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        UserMaxBidRecord userMaxBidRequest = new UserMaxBidRecord();
        userMaxBidRequest.setUserId(highestBidUserId);
        when(bidRepository.findHighestBidAndUserByProduct(anyLong(), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(userMaxBidRequest));

        when(tokenService.getAuthenticatedUser(any(HttpServletRequest.class))).thenReturn(appUser);

        bidService.createBidAndPublishToQueue(productId, amount, request);

        verify(bidRepository, times(1)).save(any(Bid.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(NotificationResponse.class));
    }

}


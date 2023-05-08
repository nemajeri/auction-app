package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.BidAmountException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.model.Bid;
import com.atlantbh.auctionappbackend.model.Product;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.repository.BidRepository;
import com.atlantbh.auctionappbackend.repository.ProductRepository;
import com.atlantbh.auctionappbackend.request.BidRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private AppUserRepository appUserRepository;

    @Mock
    private TokenService tokenService;

    @Test
    @DisplayName("Should create a new bid")
    public void testCreateBid() throws BidAmountException {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setDescription("Test Description");
        product.setStartPrice(100.00f);
        product.setStartDate(LocalDateTime.now());
        product.setEndDate(LocalDateTime.now().plusDays(1));

        Long productId = 1L;
        product.setId(productId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        BidRequest bidRequest = new BidRequest(product.getId(), 200.00f);

        HttpServletRequest request = mock(HttpServletRequest.class);

        String jwt = "jwt-token";
        when(tokenService.getJwtFromHeader(request)).thenReturn(jwt);
        when(tokenService.validateToken(jwt)).thenReturn(true);
        when(tokenService.getClaimFromToken(jwt, "sub")).thenReturn("user@example.com");

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        when(appUserRepository.getByEmail("user@example.com")).thenReturn(Optional.of(appUser));
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(appUser));

        List<Bid> createdBids = new ArrayList<>();
        when(bidRepository.save(any(Bid.class))).thenAnswer(invocation -> {
            Bid bid = invocation.getArgument(0);
            createdBids.add(bid);
            return bid;
        });
        when(bidRepository.findAll()).thenReturn(createdBids);

        bidService.createBid(bidRequest.getProductId(), bidRequest.getAmount(), request);

        List<Bid> bidList = bidRepository.findAll();
        assertEquals(1, bidList.size());

        Bid bid = bidList.get(0);
        assertEquals(200.00f, bid.getPrice());
        assertEquals(appUserRepository.findById(1L).get(), bid.getUser());
        assertEquals(product, bid.getProduct());
    }


}


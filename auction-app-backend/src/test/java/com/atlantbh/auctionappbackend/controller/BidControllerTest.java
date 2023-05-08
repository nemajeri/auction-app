package com.atlantbh.auctionappbackend.controller;


import com.atlantbh.auctionappbackend.request.BidRequest;
import com.atlantbh.auctionappbackend.service.BidService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BidControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BidService bidService;


    @Test
    public void createBid_ReturnCreatedResponse() throws Exception {
        BidRequest bidRequest = new BidRequest(2L, 50.00f);

        doNothing().when(bidService).createBid(eq(bidRequest.getProductId()), eq(bidRequest.getAmount()), any(HttpServletRequest.class));

        mockMvc.perform(post("/api/v1/bid/create-bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bidRequest).getBytes()))
                .andExpect(status().isCreated());

        verify(bidService, times(1)).createBid(eq(bidRequest.getProductId()), eq(bidRequest.getAmount()), any(HttpServletRequest.class));
    }
}
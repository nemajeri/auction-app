package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.enums.PaymentStatus;
import com.atlantbh.auctionappbackend.response.PaymentResponse;
import com.atlantbh.auctionappbackend.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;


    @Test
    public void createPayment_ReturnsPaymentResponse() throws Exception {
        String paymentMethodId = "paymentMethodId";
        long amount = 100;
        String currency = "USD";
        Long productId = 1L;

        PaymentResponse paymentResponse = new PaymentResponse("paymentId", PaymentStatus.SUCCESS.getStatus(), amount, currency);

        when(paymentService.payForProduct(amount, currency, paymentMethodId, productId)).thenReturn(paymentResponse);

        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("paymentMethodId", paymentMethodId);
        paymentData.put("amount", amount);
        paymentData.put("currency", currency);
        paymentData.put("productId", productId);

        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentResponse.getId()))
                .andExpect(jsonPath("$.status").value(paymentResponse.getStatus()))
                .andExpect(jsonPath("$.amount").value(amount))
                .andExpect(jsonPath("$.currency").value(currency));

        verify(paymentService, times(1)).payForProduct(amount, currency, paymentMethodId, productId);
    }
}




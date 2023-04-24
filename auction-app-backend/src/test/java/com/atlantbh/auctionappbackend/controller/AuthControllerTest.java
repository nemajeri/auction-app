package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.TestGoogleIdToken;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.OAuth2LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.service.AuthService;
import com.atlantbh.auctionappbackend.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MockBean
    TokenService tokenService;

    @MockBean
    AuthService authService;

    @Captor
    private ArgumentCaptor<HttpServletResponse> responseCaptor;

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Test
    public void testRegisterAppUser_ReturnsCreatedResponse() throws Exception {

        String password = encodePassword("12345");

        RegisterRequest request = new RegisterRequest(
                "John", "Doe", "john@gmail.com", password
        );

        doNothing().when(authService).register(request);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.getBytes()))
                .andExpect(status().isCreated());
    }

    @Test
    public void testLoginAppUser_ReturnsOkResponse() throws Exception {

        String password = encodePassword("12345");

        LoginRequest request = new LoginRequest(
                "john@gmail.com", password
        );

        Cookie expectedCookie = new Cookie("jwt-token", "a1b2");

        doAnswer(invocation -> {
            HttpServletResponse response = invocation.getArgument(2);
            response.addCookie(expectedCookie);
            return null;
        }).when(authService).login(any(HttpServletRequest.class), eq(request), any(HttpServletResponse.class));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request).getBytes()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse res = result.getResponse();
                    assertNotNull(res);
                    Cookie[] cookies = res.getCookies();
                    Optional<Cookie> jwtCookie = Arrays.stream(cookies).filter(c -> "jwt-token".equals(c.getName())).findFirst();
                    assertTrue(jwtCookie.isPresent());
                    assertEquals(expectedCookie.getValue(), jwtCookie.get().getValue());
                });

        verify(authService, times(1)).login(any(HttpServletRequest.class), eq(request), responseCaptor.capture());
    }

    @Test
    public void testHandleOAuth2LoginSuccess_ReturnsOkResponse() throws Exception {
        String provider = "google";
        String token = "my-oauth2-token";
        String jwt = "my-jwt-token";

        OAuth2LoginRequest oauth2LoginRequest = new OAuth2LoginRequest(provider, token);

        String json = new ObjectMapper().writeValueAsString(oauth2LoginRequest);

        Cookie jwtCookie = new Cookie("jwt", jwt);
        when(authService.processOAuth2Login(oauth2LoginRequest)).thenReturn(jwtCookie);

        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setSubject("some-subject");
        payload.setEmail("test@example.com");
        payload.set("given_name", "John");
        payload.set("family_name", "Doe");
        GoogleIdToken googleIdToken = new TestGoogleIdToken(payload);
        when(tokenService.verifyGSIToken(token)).thenReturn(googleIdToken);

        mockMvc.perform(post("/api/v1/auth/oauth2-login-success")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.getBytes()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse res = result.getResponse();
                    assertNotNull(res);
                    Cookie[] cookies = res.getCookies();
                    Optional<Cookie> jwtCookieOptional = Arrays.stream(cookies).filter(c -> "jwt".equals(c.getName())).findFirst();
                    assertTrue(jwtCookieOptional.isPresent());
                    assertEquals(jwt, jwtCookieOptional.get().getValue());
                });
    }



    @Test
    public void testLogoutAppUser_ReturnsOkResponse() throws Exception {
        String token = "my-token";
        Cookie cookie = new Cookie("auction_app_logout_token", token);

        when(tokenService.getJwtFromCookie(any())).thenReturn(token);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse res = result.getResponse();
                    assertNotNull(res);
                    Cookie[] cookies = res.getCookies();
                    Optional<Cookie> jwtCookie = Arrays.stream(cookies).filter(c -> "auction_app_logout_token".equals(c.getName())).findFirst();
                    assertTrue(jwtCookie.isPresent());
                    assertEquals("", jwtCookie.get().getValue());
                    assertEquals("/", jwtCookie.get().getPath());
                    assertEquals(0, jwtCookie.get().getMaxAge());
                });

        verify(tokenService, times(1)).invalidateToken(token);
    }
}

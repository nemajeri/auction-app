package com.atlantbh.auctionappbackend.controller;

import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.JwtAuthenticationFilter;
import com.atlantbh.auctionappbackend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
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
    AuthService authService;

    @MockBean
    JwtAuthenticationFilter jwtFilter;

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

        Cookie cookie = new Cookie("jwt-token", "a1b2");

        when(authService.login(eq(request), Matchers.any(HttpServletResponse.class))).thenReturn(cookie);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request).getBytes()))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogoutAppUser_ReturnsOkResponse() throws Exception {
        String token = "my-token";
        Cookie cookie = new Cookie("jwt-token", token);

        when(jwtFilter.getJwtFromCookie(any())).thenReturn(token);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(cookie)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    MockHttpServletResponse res = result.getResponse();
                    assertNotNull(res);
                    Cookie[] cookies = res.getCookies();
                    Optional<Cookie> jwtCookie = Arrays.stream(cookies).filter(c -> "jwt-token".equals(c.getName())).findFirst();
                    assertTrue(jwtCookie.isPresent());
                    assertEquals("", jwtCookie.get().getValue());
                    assertEquals("/", jwtCookie.get().getPath());
                    assertEquals(0, jwtCookie.get().getMaxAge());
                });

        verify(jwtFilter, times(1)).invalidateToken(token);
    }
}
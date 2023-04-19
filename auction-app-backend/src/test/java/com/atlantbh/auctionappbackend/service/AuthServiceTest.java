package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.DuplicateAppUserException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.request.LoginRequest;
import com.atlantbh.auctionappbackend.request.OAuth2LoginRequest;
import com.atlantbh.auctionappbackend.request.RegisterRequest;
import com.atlantbh.auctionappbackend.security.jwt.JwtTokenProvider;
import com.atlantbh.auctionappbackend.utils.TokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenVerifier tokenVerifier;

    @Mock
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("Should add user to database")
    public void testRegisterNewAppUser() throws DuplicateAppUserException {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123");

        when(appUserRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encoded_password");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(new AppUser());

        authService.register(registerRequest);

        verify(appUserRepository, times(1)).existsByEmail(registerRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(registerRequest.getPassword());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should add cookie to response when user logs in")
    public void testLogin() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", passwordEncoder.encode("password123"));
        AppUser appUser = new AppUser(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                passwordEncoder.encode("password123")
        );

        when(appUserRepository.getByEmail(loginRequest.getEmail())).thenReturn(Optional.of(appUser));
        when(passwordEncoder.matches(eq(loginRequest.getPassword()), eq(appUser.getPassword()))).thenReturn(true);

        authService.login(request, loginRequest, response);

        verify(appUserRepository, times(1)).getByEmail(loginRequest.getEmail());
        verify(passwordEncoder, times(1)).matches(eq(loginRequest.getPassword()), eq(appUser.getPassword()));
        verify(response, times(1)).addCookie(argThat(cookie -> cookie.getName().equals("jwt")
                && cookie.getMaxAge() == 4 * 60 * 60
                && !cookie.isHttpOnly()
                && cookie.getPath().equals("/")
                && !cookie.getSecure()));
    }

    @Test
    public void processOAuth2Login_GoogleProvider() {
        String provider = "google";
        String token = "test-token";
        OAuth2LoginRequest oauth2LoginRequest = new OAuth2LoginRequest(provider, token);

        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.set("given_name", "John");
        payload.set("family_name", "Doe");
        payload.setSubject("12345");
        payload.setEmail("john.doe@example.com");

        when(tokenVerifier.verifyGSIToken(token)).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);

        AppUser appUser = new AppUser();
        appUser.setEmail("john.doe@example.com");
        appUser.setFirstName("John");
        appUser.setLastName("Doe");
        appUser.setPassword("");

        when(appUserRepository.findByEmail("john.doe@example.com")).thenReturn(appUser);
        when(jwtTokenProvider.generateToken(any())).thenReturn("sample-jwt-token");

        Cookie result = authService.processOAuth2Login(oauth2LoginRequest);

        assertEquals("jwt-token", result.getName());
        assertEquals("sample-jwt-token", result.getValue());
        assertEquals("/", result.getPath());
        assertEquals(4 * 60 * 60, result.getMaxAge());
    }
}

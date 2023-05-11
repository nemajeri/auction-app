package com.atlantbh.auctionappbackend.service;

import com.atlantbh.auctionappbackend.exception.AppUserNotFoundException;
import com.atlantbh.auctionappbackend.model.AppUser;
import com.atlantbh.auctionappbackend.repository.AppUserRepository;
import com.atlantbh.auctionappbackend.response.AppUserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserResponse getUserByEmail(String email) {
        AppUser appUser = appUserRepository.getByEmail(email)
                .orElseThrow(() -> new AppUserNotFoundException("User not found with email: " + email));

        return AppUserResponse.builder()
                .id(appUser.getId())
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .city(appUser.getCity())
                .zipCode(appUser.getZipCode())
                .country(appUser.getCountry())
                .phone(appUser.getPhone())
                .address(appUser.getAddress())
                .build();
    }
}


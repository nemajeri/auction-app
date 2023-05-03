package com.atlantbh.auctionappbackend.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String city;

    private String zipCode;

    private String country;

    private String phone;
}

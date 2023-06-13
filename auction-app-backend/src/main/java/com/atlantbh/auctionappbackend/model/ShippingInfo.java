package com.atlantbh.auctionappbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInfo {

    private String city;

    private String address;

    private String country;

    private String zipCode;

    private String phone;
}

package com.atlantbh.auctionappbackend.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
public class NewProductRequest {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category ID is required")
    private String categoryId;

    private String subcategoryId;

    @NotBlank(message = "Start price is required")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Start price must be a valid number")
    private String startPrice;

    @NotNull(message = "Start date is required")
    private ZonedDateTime startDate;

    @NotNull(message = "End date is required")
    private ZonedDateTime endDate;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Zip code is required")
    private String zipCode;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "\\+\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    private String phone;

}

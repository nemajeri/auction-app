package com.atlantbh.auctionappbackend.request;

import com.atlantbh.auctionappbackend.model.Image;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class CsvProductRequest {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Subcategory name is required")
    private String subcategoryName;

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

    @NotBlank(message = "Images are required")
    private String images;

    public List<Image> getImages() {
         List<String> stringList = Arrays.stream(images.split(";"))
                .map(String::trim).toList();

         return stringList.stream().map(imageFromList -> {
            Image image = new Image();
            image.setImageUrl(imageFromList);
             return image;
         }).toList();
    }
}

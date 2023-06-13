package com.atlantbh.auctionappbackend.dto;

import com.atlantbh.auctionappbackend.utils.FloatConverter;
import com.atlantbh.auctionappbackend.utils.ZonedDateTimeConverter;
import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ProductCsvImport {

    @CsvBindByName(column = "Product Name")
    private String productName;

    @CsvBindByName( column = "Description")
    private String description;

    @CsvBindByName( column = "Category Name")
    private String categoryName;

    @CsvBindByName( column = "Subcategory Name")
    private String subcategoryName;

    @CsvCustomBindByName(converter = FloatConverter.class, column = "Start Price")
    private Float startPrice;

    @CsvCustomBindByName(converter = ZonedDateTimeConverter.class, column = "Start Date")
    private ZonedDateTime startDate;

    @CsvCustomBindByName(converter = ZonedDateTimeConverter.class, column = "End Date")
    private ZonedDateTime endDate;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "City")
    private String city;

    @CsvBindByName(column = "Zip Code")
    private String zipCode;

    @CsvBindByName(column = "Country")
    private String country;

    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "\\+\\d{10,15}")
    @CsvBindByName(column = "Phone")
    private String phone;

    @CsvBindAndSplitByName(elementType = String.class, splitOn = "\\s*;\\s*", collectionType = ArrayList.class, column = "Images")
    private List<String> images;
}

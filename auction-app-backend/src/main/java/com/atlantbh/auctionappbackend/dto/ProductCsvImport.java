package com.atlantbh.auctionappbackend.dto;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.validators.MustMatchRegexExpression;
import com.opencsv.bean.validators.PreAssignmentValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ProductCsvImport {

    @CsvBindByName(column = "Product Name")
    private String productName;

    @CsvBindByName( column = "Description")
    private String description;

    @CsvBindByName( column = "Category Name")
    private String categoryName;

    @CsvBindByName( column = "Subcategory Name")
    private String subcategoryName;

    @CsvBindByName( column = "Start Price")
    private String startPrice;

    @CsvBindByName( column = "Start Date")
    private String startDate;

    @CsvBindByName(column = "End Date")
    private String endDate;

    @CsvBindByName(column = "Address")
    private String address;

    @CsvBindByName(column = "City")
    private String city;

    @PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^\\d{6}$")
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

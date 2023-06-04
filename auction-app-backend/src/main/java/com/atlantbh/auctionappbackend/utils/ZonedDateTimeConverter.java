package com.atlantbh.auctionappbackend.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class ZonedDateTimeConverter extends AbstractBeanField<String,ZonedDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_ZONED_DATE_TIME;

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        try {
            return ZonedDateTime.parse(value, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
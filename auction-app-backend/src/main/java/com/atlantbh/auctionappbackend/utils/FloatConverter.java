package com.atlantbh.auctionappbackend.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class FloatConverter extends AbstractBeanField<String, Float> {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new CsvDataTypeMismatchException(e.getMessage());
        }
    }
}
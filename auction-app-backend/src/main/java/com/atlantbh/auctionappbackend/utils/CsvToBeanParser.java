package com.atlantbh.auctionappbackend.utils;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CsvToBeanParser<T> {

    public List<T> parse(Reader reader, Class<T> type) {
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(type)
                .withIgnoreLeadingWhiteSpace(true)
                .build();
        return csvToBean.parse();
    }
}
package com.example.service.client;

import com.example.service.constant.ApplicationConstants;
import com.example.service.model.external.Rates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Currency;

@Log4j2
@Component
public class MockCurrencyConversionClient implements CurrencyConversionClient {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public Rates getRates(Currency baseCurrency, LocalDate date) {
        OBJECT_MAPPER
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());

        try {
            return OBJECT_MAPPER.readValue(ApplicationConstants.currencyConversionResponse, Rates.class);
        } catch (Exception e) {
            log.error(e);
        }
        return null;
    }
}

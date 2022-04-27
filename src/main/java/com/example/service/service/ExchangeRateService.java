package com.example.service.service;

import com.example.service.model.ConversionRequest;
import com.example.service.model.ExchangeRate;

import java.util.concurrent.CompletableFuture;

public interface ExchangeRateService {
    public CompletableFuture<ExchangeRate> getExchangeRate(ConversionRequest request);
}

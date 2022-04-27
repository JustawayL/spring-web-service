package com.example.service.service;

import com.example.service.client.CurrencyConversionClient;
import com.example.service.model.ConversionRequest;
import com.example.service.model.ExchangeRate;
import com.example.service.model.external.Rates;
import com.example.service.util.CurrencyUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private CurrencyConversionClient currencyConversionClient;

    @Override
    public CompletableFuture<ExchangeRate> getExchangeRate(ConversionRequest request) {

        List<String> toCurrencies =
                request.getTo().stream().map(Object::toString).collect(Collectors.toList());

        return CompletableFuture.completedFuture(
                        currencyConversionClient.getRates(request.getFrom(), request.getDate()))
                .thenApply(
                        response -> {
                            if (request.getDate() == null && request.getFrom().toString().equals("USD")) {
                                Optional<Double> rateValue =
                                        response.getRates().entrySet().stream()
                                                .filter(key -> key.getKey().equals("CAD"))
                                                .map(Map.Entry::getValue)
                                                .findFirst();
                            }
                            return filterRates(response, toCurrencies);
                        })
                .thenApply(rates -> toConversionResponse(request, rates));
    }


    private ExchangeRate toConversionResponse(ConversionRequest request, Rates rates) {

        Map<Currency, Double> responseRates =
                rates.getRates().entrySet().stream()
                        .collect(
                                Collectors.toMap(
                                        e -> CurrencyUtils.parseCurrency(e.getKey()),
                                        Map.Entry::getValue));

        LocalDate date = Optional.ofNullable(request.getDate()).orElse(LocalDate.now());

        return ExchangeRate.builder()
                .date(date)
                .timestamp(rates.getTimestamp())
                .from(request.getFrom())
                .bankMarketRates(responseRates)
                .consumerRates(createConsumerRates(request.getFrom(), responseRates))
                .build();
    }

    private Map<Currency, Double> createConsumerRates(
            Currency from, Map<Currency, Double> bankMarketRates) {
        return bankMarketRates.entrySet().stream()
                .map(
                        entry ->
                                Map.entry(
                                        entry.getKey(),
                                        entry.getValue()))
                .collect(
                        Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue));
    }

    private Rates filterRates(Rates responseRates, List<String> currencies) {
        Map<String, Double> filteredRates =
                responseRates.getRates().entrySet().stream()
                        .filter(entry -> CurrencyUtils.isValid(entry.getKey()))
                        .filter(entry -> currencies.isEmpty() || currencies.contains(entry.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Rates rates = new Rates();
        rates.setTimestamp(responseRates.getTimestamp());
        rates.setRates(filteredRates);
        return rates;
    }
}

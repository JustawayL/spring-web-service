package com.example.service.controller;

import static com.example.service.constant.ApplicationConstants.APPLICATION_JSON;

import com.example.service.model.ConversionRequest;
import com.example.service.model.ExchangeRate;
import com.example.service.service.ExchangeRateService;
import com.example.service.util.CurrencyUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log4j2
public class ExchangeRateController {

  private final ExchangeRateService exchangeRateService;

  @Async
  @ApiOperation(value = "Gets conversion rates for currencies")
  @RequestMapping(value = "convert", method = RequestMethod.GET, produces = APPLICATION_JSON)
  public CompletableFuture<ResponseEntity<ExchangeRate>> convertCurrency(
      @ApiParam(value = "The currency to which exchange rates are relative to", required = true)
          @RequestParam
          String from,
      @ApiParam(value = "The currency an amount is converted to.") @RequestParam(required = false)
          String to,
      @ApiParam(
              value =
                  "Date of conversion rate yyyy-MM-dd. A date for which exchange rates are requested.")
          @RequestParam(required = false)
          @DateTimeFormat(pattern = "yyyy-MM-dd")
          LocalDate date) {

    ConversionRequest.ConversionRequestBuilder requestBuilder =
        ConversionRequest.builder().from(CurrencyUtils.parseCurrencyWithException(from));
    Optional.ofNullable(date).ifPresent(requestBuilder::date);

    Optional<List<Currency>> currencies =
        Optional.ofNullable(to)
            .flatMap(s -> s.isBlank() ? Optional.empty() : Optional.of(s))
            .map(toCurrencies -> Arrays.asList(toCurrencies.split("\\s*,\\s*")))
            .map(
                toCurrenciesList ->
                    toCurrenciesList.stream()
                        .map(CurrencyUtils::parseCurrencyWithException)
                        .collect(Collectors.toList()));

    requestBuilder.to(currencies.orElse(Collections.emptyList()));

    ConversionRequest request = requestBuilder.build();
    log.info("Exchange Rate Request {}", request);
    return exchangeRateService.getExchangeRate(request).thenApply(ResponseEntity::ok);
  }
}

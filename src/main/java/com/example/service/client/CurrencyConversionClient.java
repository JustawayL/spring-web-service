package com.example.service.client;

import com.example.service.model.external.Rates;
import java.time.LocalDate;
import java.util.Currency;

public interface CurrencyConversionClient {
  public Rates getRates(Currency baseCurrency, LocalDate date);
}

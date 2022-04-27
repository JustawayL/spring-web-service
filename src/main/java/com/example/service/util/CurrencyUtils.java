package com.example.service.util;

import lombok.extern.log4j.Log4j2;

import java.util.Currency;

@Log4j2
public class CurrencyUtils {

  public static Currency parseCurrency(String currency) {

    return Currency.getInstance(currency);
  }

  public static Currency parseCurrencyWithException(String currency) {
    try {
      return Currency.getInstance(currency);
    } catch (IllegalArgumentException e) {
      log.error(e);
    }
    return null;
  }

  public static boolean isValid(String currency) {
    try {
      parseCurrency(currency);
      return true;
    } catch (Exception e) {
    }
    return false;
  }
}

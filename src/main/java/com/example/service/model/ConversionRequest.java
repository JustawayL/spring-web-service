package com.example.service.model;

import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
public class ConversionRequest {
  private Currency from;
  private List<Currency> to;
  LocalDate date;
}

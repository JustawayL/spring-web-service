package com.example.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExchangeRate {

  @ApiModelProperty(example = "USD")
  private Currency from;

  Instant timestamp;

  @ApiModelProperty(example = "2020-08-13")
  LocalDate date;

  @ApiModelProperty(example = "{'CHF': 0.953058, 'CAD' : 1.280046}")
  Map<Currency, Double> bankMarketRates;

  @ApiModelProperty(example = "{'CHF': 0.933058, 'CAD' : 1.260046}")
  Map<Currency, Double> consumerRates;
}

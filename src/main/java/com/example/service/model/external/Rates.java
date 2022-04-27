package com.example.service.model.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rates {

  @JsonProperty("base")
  String base;

  @JsonProperty("date")
  LocalDate date;

  @JsonProperty("timestamp")
  Instant timestamp;

  @JsonProperty("rates")
  Map<String, Double> rates;
}

package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class PriceOverview {
  @JsonProperty("currency")
  private String currency;

  @JsonProperty("initial")
  private Integer initialPrice;

  @JsonProperty("final")
  private Integer finalPrice;

  @JsonProperty("discount_percent")
  private Integer discountPercent;

  @JsonProperty("initial_formatted")
  private String initialFormatted;

  @JsonProperty("final_formatted")
  private String finalFormatted;
}

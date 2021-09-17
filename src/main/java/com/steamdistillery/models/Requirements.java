package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Requirements {
  @JsonProperty("minimum")
  @Lob
  private String minimum;

  @JsonProperty("recommended")
  @Lob
  private String recommended;
}

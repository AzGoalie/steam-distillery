package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ReleaseDate {
  @JsonProperty("coming_soon")
  private boolean comingSoon;

  @JsonProperty("date")
  private String date;
}

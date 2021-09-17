package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Metacritic {
  @JsonProperty("score")
  private Integer score;

  @JsonProperty("url")
  private String url;
}

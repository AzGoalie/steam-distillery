package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Fullgame {
  @JsonProperty("appid")
  private String appid;

  @JsonProperty("name")
  private String name;
}

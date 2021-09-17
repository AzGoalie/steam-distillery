package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Demo {
  @JsonProperty("appid")
  private int appid;

  @JsonProperty("description")
  private String description;
}

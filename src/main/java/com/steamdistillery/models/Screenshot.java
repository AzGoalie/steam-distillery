package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Screenshot {
  @JsonProperty("id")
  private int id;

  @JsonProperty("path_thumbnail")
  private String thumbnail;

  @JsonProperty("path_full")
  private String full;
}

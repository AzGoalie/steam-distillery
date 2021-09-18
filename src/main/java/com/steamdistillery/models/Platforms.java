package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Platforms {
  @JsonProperty("windows")
  private Boolean windows;

  @JsonProperty("mac")
  private Boolean mac;

  @JsonProperty("linux")
  private Boolean linux;
}

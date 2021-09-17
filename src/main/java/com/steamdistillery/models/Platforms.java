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
  private boolean windows;

  @JsonProperty("mac")
  private boolean mac;

  @JsonProperty("linux")
  private boolean linux;
}

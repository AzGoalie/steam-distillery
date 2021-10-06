package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SupportInfo {
  @JsonProperty("url")
  private String url;

  @JsonProperty("email")
  private String email;
}

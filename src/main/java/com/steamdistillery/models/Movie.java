package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Movie {
  @JsonProperty("id")
  private int id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("thumbnail")
  private String thumbnail;

  @JsonProperty("webm")
  @Embedded
  private Format webm;

  @JsonProperty("mp4")
  @Embedded
  private Format mp4;

  @JsonProperty("highlight")
  private boolean highlight;

  @Embeddable
  @Getter
  @Setter
  public static class Format {
    @JsonProperty("480")
    private String lowRes;

    @JsonProperty("max")
    private String highRes;
  }
}

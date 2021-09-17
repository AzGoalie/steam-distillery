package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Achievements {
  @JsonProperty("total")
  private Integer total;

  @JsonProperty("highlighted")
  @ElementCollection
  private List<Achievement> highlighted;

  @Embeddable
  @Getter
  public static class Achievement {
    @JsonProperty("name")
    private String name;

    @JsonProperty("path")
    private String path;
  }
}

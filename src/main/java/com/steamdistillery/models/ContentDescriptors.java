package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ContentDescriptors {
  @JsonProperty("ids")
  @ElementCollection
  private List<Integer> ids;

  @JsonProperty("notes")
  @Lob
  private String notes;
}

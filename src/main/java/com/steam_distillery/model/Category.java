package com.steam_distillery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {

  @Id
  @JsonProperty
  private Long id;

  @Column(nullable = false)
  private String description;

  protected Category() {
  }

  protected Category(String description) {
    this.description = description;
  }

  public Category(Long id, String description) {
    this.id = id;
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return id.equals(category.id) && description.equals(category.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description);
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", description='" + description + '\'' + '}';
  }
}

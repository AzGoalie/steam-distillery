package com.steam_distillery.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;

@NamedEntityGraph(name = "SteamApp.categories", attributeNodes = {
    @NamedAttributeNode("categories")})
@Entity
public class SteamApp {
  @Id
  @JsonAlias({"steam_appid"})
  private Long appid;

  private String name;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Category> categories = new HashSet<>();

  protected SteamApp() {
  }

  public SteamApp(Long appid, String name, Set<Category> categories) {
    this.appid = appid;
    this.name = name;
    this.categories = categories;
  }

  public Long getAppid() {
    return appid;
  }

  public String getName() {
    return name;
  }

  public Set<Category> getCategories() {
    return Collections.unmodifiableSet(categories);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteamApp steamApp = (SteamApp) o;
    return appid.equals(steamApp.appid) && name.equals(steamApp.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(appid, name);
  }

  @Override
  public String toString() {
    return "SteamApp{" +
        "appid=" + appid +
        ", name='" + name + '\'' +
        ", categories=" + categories +
        '}';
  }
}

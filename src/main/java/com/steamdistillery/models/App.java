package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class App {
  @Id
  @GeneratedValue
  private long id;

  @JsonProperty("type")
  private String type;

  @JsonProperty("name")
  private String name;

  @JsonProperty("steam_appid")
  private int appid;

  @JsonProperty("required_age")
  private int requiredAge;

  @JsonProperty("controller_support")
  private String controllerSupport;

  @JsonProperty("dlc")
  @ElementCollection
  private List<Integer> dlc;

  @JsonProperty("detailed_description")
  @Lob
  private String detailedDescription;

  @JsonProperty("about_the_game")
  @Lob
  private String aboutTheGame;

  @JsonProperty("short_description")
  @Lob
  private String shortDescription;

  @JsonProperty("fullgame")
  @Embedded
  private Fullgame fullgame;

  @JsonProperty("supported_languages")
  @Lob
  private String supportedLanguages;

  @JsonProperty("header_image")
  private String headerImage;

  @JsonProperty("website")
  private String website;

  @JsonProperty("pc_requirements")
  @Embedded
  private Requirements pcRequirements;

  @JsonProperty("mac_requirements")
  @Embedded
  private Requirements macRequirements;

  @JsonProperty("linux_requirements")
  @Embedded
  private Requirements linuxRequirements;

  @JsonProperty("legal_notice")
  @Lob
  private String legalNotice;

  @JsonProperty("developers")
  @ElementCollection
  private List<String> developers;

  @JsonProperty("publishers")
  @ElementCollection
  private List<String> publishers;

  @JsonProperty("demos")
  @ElementCollection
  private List<Demo> demos;

  @JsonProperty("price_overview")
  @Embedded
  private PriceOverview priceOverview;

  @JsonProperty("packages")
  @ElementCollection
  private List<Integer> packages;

  @JsonProperty("package_groups")
  @OneToMany(cascade = CascadeType.ALL)
  private List<PackageGroup> packageGroups;

  @JsonProperty("platforms")
  @Embedded
  private Platforms platforms;

  @JsonProperty("metacritic")
  @Embedded
  private Metacritic metacritic;

  @JsonProperty("categories")
  @ElementCollection
  private List<Category> categories;

  @JsonProperty("genres")
  @ElementCollection
  private List<Genre> genres;

  @JsonProperty("screenshots")
  @ElementCollection
  private List<Screenshot> screenshots;

  @JsonProperty("movies")
  @ElementCollection
  private List<Movie> movies;

  @JsonProperty("recommendations")
  @Embedded
  private Recommendations recommendations;

  @JsonProperty("achievements")
  @Embedded
  private Achievements achievements;

  @JsonProperty("release_date")
  @Embedded
  private ReleaseDate releaseDate;

  @JsonProperty("support_info")
  @Embedded
  private SupportInfo supportInfo;

  @JsonProperty("background")
  private String background;

  @JsonProperty("content_descriptors")
  @Embedded
  private ContentDescriptors contentDescriptors;
}

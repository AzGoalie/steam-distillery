package com.steamdistillery.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PackageGroup {
  @Id
  @GeneratedValue
  private long id;

  @ManyToMany
  private List<App> apps;

  @JsonProperty("name")
  private String name;

  @JsonProperty("title")
  private String title;

  @JsonProperty("description")
  private String description;

  @JsonProperty("selection_text")
  private String selectionText;

  @JsonProperty("save_text")
  private String saveText;

  @JsonProperty("display_type")
  private int displayType;

  @JsonProperty("is_recurring_subscription")
  private String isRecurringSubscription;

  @JsonProperty("subs")
  @ElementCollection
  private List<Sub> subs;

  @Embeddable
  @Getter
  @Setter
  public static class Sub {
    @JsonProperty("packageid")
    private int packageId;

    @JsonProperty("percent_savings_text")
    private String percentSavingsText;

    @JsonProperty("percent_savings")
    private int percentSavings;

    @JsonProperty("option_text")
    private String optionText;

    @JsonProperty("option_description")
    private String optionDescription;

    @JsonProperty("can_get_free_license")
    private String canGetFreeLicense;

    @JsonProperty("is_free_license")
    private boolean isFreeLicense;

    @JsonProperty("price_in_cents_with_discount")
    private int priceInCentsWithDiscount;
  }
}

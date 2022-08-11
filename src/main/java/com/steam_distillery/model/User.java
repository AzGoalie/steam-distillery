package com.steam_distillery.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Collections;
import java.util.Set;

public record User(long steamid, @JsonAlias("personaname") String name, Set<SteamApp> ownedApps) {

  public User {
    if (ownedApps == null) {
      ownedApps = Collections.emptySet();
    }
  }

  public User(long steamid, String name) {
    this(steamid, name, Collections.emptySet());
  }
}

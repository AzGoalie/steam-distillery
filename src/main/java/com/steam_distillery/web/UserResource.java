package com.steam_distillery.web;

import com.steam_distillery.model.SteamApp;
import com.steam_distillery.model.User;
import com.steam_distillery.service.SteamApiService;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UserResource {

  private static final Logger log = LoggerFactory.getLogger(SteamAppResource.class);

  private final SteamApiService apiService;

  public UserResource(SteamApiService apiService) {
    this.apiService = apiService;
  }

  @QueryMapping
  Set<User> users(@Argument List<Long> steamids) {
    log.info("Fetching user info for steamids {}", steamids);
    return apiService.getPlayerSummaries(steamids);
  }

  @QueryMapping
  Optional<User> user(@Argument long steamid) {
    log.info("Fetching user info for steamid {}", steamid);
    return apiService.getPlayerSummary(steamid);
  }

  @BatchMapping(typeName = "User")
  Map<User, Set<SteamApp>> ownedApps(Set<User> users) {
    return users.stream().map(user -> Map.entry(user, apiService.getOwnedApps(user.steamid())))
        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
  }
}

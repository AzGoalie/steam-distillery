package com.steam_distillery.web;

import static org.mockito.Mockito.when;

import com.steam_distillery.model.Category;
import com.steam_distillery.model.SteamApp;
import com.steam_distillery.model.User;
import com.steam_distillery.service.SteamApiService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(UserResource.class)
public class UserResourceTest {

  @Autowired
  private GraphQlTester graphQlTester;

  @MockBean
  private SteamApiService apiService;

  @Test
  public void testGetSpecificUser() {
    final var steamid = 1234567890L;
    final var user = new User(steamid, "Test User");

    when(apiService.getPlayerSummary(steamid)).thenReturn(Optional.of(user));

    graphQlTester
        .documentName("user")
        .variable("steamid", steamid)
        .execute()
        .path("user")
        .entity(User.class)
        .isEqualTo(user);
  }

  @Test
  public void testGetUsers() {
    final var counterStrike = new SteamApp(10L, "Counter-Strike",
        Set.of(new Category(36L, "Online PvP")));
    final var halfLife = new SteamApp(70L, "Half-Life",
        Set.of(new Category(2L, "Single-player"), new Category(36L, "Online PvP")));

    final var user1 = new User(1234567890L, "Test User 1");
    final var user2 = new User(9876543210L, "Test User 2");
    final var steamids = List.of(user1.steamid(), user2.steamid());

    when(apiService.getPlayerSummaries(steamids)).thenReturn(Set.of(user1, user2));
    when(apiService.getOwnedApps(user1.steamid())).thenReturn(Set.of(counterStrike));
    when(apiService.getOwnedApps(user2.steamid())).thenReturn(Set.of(halfLife));

    graphQlTester
        .documentName("users")
        .variable("steamids", steamids)
        .execute()
        .path("users")
        .entityList(User.class)
        .contains(new User(user1.steamid(), user1.name(), Set.of(counterStrike)))
        .contains(new User(user2.steamid(), user2.name(), Set.of(halfLife)));
  }
}

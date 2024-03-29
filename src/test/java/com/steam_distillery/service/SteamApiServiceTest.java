package com.steam_distillery.service;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.steam_distillery.model.Category;
import com.steam_distillery.model.SteamApp;
import com.steam_distillery.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "steam.api-key=1234567890",
    "steam.api-base-url=http://localhost:9876/api",
    "steam.app-details-url=http://localhost:9876/api/appdetails"})
@WireMockTest(httpPort = 9876)
public class SteamApiServiceTest {

  private static final SteamApp COUNTER_STRIKE = new SteamApp(10L, "Counter-Strike");
  private static final SteamApp HALF_LIFE = new SteamApp(70L, "Half-Life",
      Set.of(new Category(2L, "Single-player"), new Category(36L, "Online PvP")));

  @Value("${steam.api-key}")
  String apiKey;

  @Autowired
  private SteamApiService apiService;

  @Test
  public void testGetSteamApp() {
    stubFor(get(urlPathEqualTo("/api/appdetails"))
        .withQueryParam("appids", equalTo("70"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("half-life.json")));

    Optional<SteamApp> result = apiService.getSteamApp(HALF_LIFE.getAppid());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getAppid()).isEqualTo(HALF_LIFE.getAppid());
    assertThat(result.get().getName()).isEqualTo(HALF_LIFE.getName());
    assertThat(result.get().getCategories()).containsAll(HALF_LIFE.getCategories());
  }

  @Test
  public void testGetAllSteamApps() {
    stubFor(get(urlPathEqualTo("/api/ISteamApps/GetAppList/v2"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("applist.json")));

    Set<SteamApp> results = apiService.getAllSteamApps();
    assertThat(results).hasSize(2);
    assertThat(results).contains(COUNTER_STRIKE);
  }

  @Test
  public void testGetOwnedApps() {
    final long steamid = 1234567890L;

    stubFor(get(urlPathEqualTo("/api/IPlayerService/GetOwnedGames/v1/"))
        .withQueryParam("key", equalTo(apiKey))
        .withQueryParam("steamid", equalTo(Long.toString(steamid)))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("owned-games.json")));

    Set<SteamApp> apps = apiService.getOwnedApps(steamid);
    assertThat(apps).hasSize(2);
    assertThat(apps).contains(COUNTER_STRIKE);
  }

  @Test
  public void testGetPlayerSummaries() {
    final var steamids = List.of(1234567890L, 9876543210L);
    final var steamidsStr = steamids.stream().map(id -> Long.toString(id))
        .collect(Collectors.joining(","));

    stubFor(get(urlPathEqualTo("/api/ISteamUser/GetPlayerSummaries/v2/"))
        .withQueryParam("key", equalTo(apiKey))
        .withQueryParam("steamids", equalTo(steamidsStr))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("player-summaries.json")));

    Set<User> users = apiService.getPlayerSummaries(steamids);
    assertThat(users).hasSize(2);
    assertThat(users).contains(new User(steamids.get(0), "Test User 1"));
    assertThat(users).contains(new User(steamids.get(1), "Test User 2"));
  }

  @Test
  public void testGetPlayerSummary() {
    final var steamid = 1234567890L;

    stubFor(get(urlPathEqualTo("/api/ISteamUser/GetPlayerSummaries/v2/"))
        .withQueryParam("key", equalTo(apiKey))
        .withQueryParam("steamids", equalTo(Long.toString(steamid)))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("player-summary.json")));

    Optional<User> user = apiService.getPlayerSummary(steamid);
    assertThat(user).isPresent();
    assertThat(user).contains(new User(steamid, "Test User"));
  }
}
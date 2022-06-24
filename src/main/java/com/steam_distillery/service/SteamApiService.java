package com.steam_distillery.service;

import com.steam_distillery.model.SteamApp;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SteamApiService {
  private static final Logger log = LoggerFactory.getLogger(SteamApiService.class);

  @Value("${steam.app-details-url}")
  private String appDetailsUrl;

  @Value("${steam.api-base-url}")
  private String apiBaseUrl;

  @Value("${steam.api-key}")
  String apiKey;

  private final WebClient client;

  public SteamApiService(WebClient.Builder builder,
      @Value("${max-buffer-size}") int maxBufferSize) {
    client = builder.codecs(
            codecConfigurer -> codecConfigurer.defaultCodecs().maxInMemorySize(maxBufferSize))
        .build();
  }

  public Set<SteamApp> getAllSteamApps() {
    log.info("Fetching all appids");

    record Response(AppList applist) {
      record AppList(Set<SteamApp> apps) {
      }
    }

    Set<SteamApp> apps = client
        .get()
        .uri(apiBaseUrl + "/ISteamApps/GetAppList/v2")
        .retrieve()
        .bodyToMono(Response.class)
        .log()
        .map(response -> response.applist.apps)
        .onErrorReturn(Collections.emptySet())
        .block();

    log.info("Fetched {} apps", apps.size());
    return apps;
  }

  public Optional<SteamApp> getSteamApp(long appid) {
    log.info("Fetching details for appid {}", appid);

    record Response(boolean success, SteamApp data) {
    }

    return client.get()
        .uri(appDetailsUrl, uriBuilder -> uriBuilder.queryParam("appids", appid).build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<Long, Response>>() {
        })
        .delayElement(Duration.ofSeconds(2))
        .map(map -> map.get(appid))
        .filter(Response::success)
        .map(Response::data)
        .blockOptional();
  }

  public Set<SteamApp> getOwnedApps(long steamid) {

    record Response(OwnedGames response) {
      record OwnedGames(long game_count, Set<SteamApp> games) {
      }
    }

    return client
        .get()
        .uri(apiBaseUrl + "/IPlayerService/GetOwnedGames/v1/",
            uriBuilder -> uriBuilder
                .queryParam("key", apiKey)
                .queryParam("steamid", steamid)
                .queryParam("include_appinfo", true)
                .queryParam("include_played_free_games", false)
                .queryParam("appids_filter", Collections.emptySet())
                .build())
        .retrieve()
        .bodyToMono(Response.class)
        .map(response -> response.response.games)
        .onErrorReturn(Collections.emptySet())
        .block();
  }
}

package com.steam_distillery.service;

import com.steam_distillery.model.SteamApp;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SteamApiService {
  private static final Logger log = LoggerFactory.getLogger(SteamApiService.class);

  private static final String ALL_APPS_URL = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";
  private static final String APP_DETAILS_URL = "https://store.steampowered.com/api/appdetails";

  private final WebClient client;

  public SteamApiService(WebClient.Builder builder,
      @Value("${max-buffer-size}") int maxBufferSize) {
    client = builder.codecs(
            codecConfigurer -> codecConfigurer.defaultCodecs().maxInMemorySize(maxBufferSize))
        .build();
  }

  public Set<Long> getAllSteamAppids() {
    log.info("Fetching all appids");

    Set<Long> appids = client
        .get()
        .uri(ALL_APPS_URL)
        .retrieve()
        .bodyToMono(AppListResponse.class)
        .map(response -> response.applist.apps.stream().map(SteamAppidName::appid).collect(
            Collectors.toSet()))
        .onErrorReturn(Collections.emptySet())
        .block();

    log.info("Fetched {} appids", appids.size());
    return appids;
  }

  public Optional<SteamApp> getSteamApp(long appid) {
    log.info("Fetching details for appid {}", appid);

    return client.get()
        .uri(APP_DETAILS_URL, uriBuilder -> uriBuilder.queryParam("appids", appid).build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<Long, AppDetailsResponse>>() {
        })
        .delayElement(Duration.ofSeconds(2))
        .map(map -> map.get(appid))
        .filter(AppDetailsResponse::success)
        .map(AppDetailsResponse::data)
        .blockOptional();
  }

  private record AppListResponse(AppList applist) {
    record AppList(List<SteamAppidName> apps) {
    }
  }

  private record SteamAppidName(long appid, String name) {
  }

  private record AppDetailsResponse(boolean success, SteamApp data) {
  }
}

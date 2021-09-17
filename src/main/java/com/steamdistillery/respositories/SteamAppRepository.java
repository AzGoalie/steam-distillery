package com.steamdistillery.respositories;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.steamdistillery.models.App;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class SteamAppRepository {

  private static final String ALL_APP_URL = "https://api.steampowered.com/ISteamApps/GetAppList/v2/";

  private static final String APP_DETAILS_URL = "https://store.steampowered.com/api/appdetails/?appids=";

  private final WebClient client;

  public SteamAppRepository(WebClient.Builder builder) {
    client = builder
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
        .build();
  }

  public Flux<SteamApp> getApps() {
    record AppList(@JsonProperty("apps") List<SteamApp> apps) {
    }
    record AppListResponse(@JsonProperty("applist") AppList appList) {
    }

    return client
        .get()
        .uri(ALL_APP_URL)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(AppListResponse.class)
        .map(AppListResponse::appList)
        .map(AppList::apps)
        .switchIfEmpty(Mono.just(Collections.emptyList()))
        .flatMapMany(Flux::fromIterable);
  }

  public Mono<App> getAppDetails(int appid) {
    record AppDetailsResponse(
        @JsonProperty("success") boolean success,
        @JsonProperty("data") App app) {
    }

    return client
        .get()
        .uri(APP_DETAILS_URL + appid)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, AppDetailsResponse>>() {
        })
        .map(map -> map.get(Integer.toString(appid)))
        .filter(AppDetailsResponse::success)
        .map(AppDetailsResponse::app)
        .onErrorContinue(
            (e, appResponse) -> log.error("Failed to get app details for " + appResponse, e));
  }

  public record SteamApp(
      @JsonProperty("appid") int appid,
      @JsonProperty("name") String name) {
  }
}

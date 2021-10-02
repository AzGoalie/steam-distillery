package com.steamdistillery.utils;

import com.steamdistillery.models.App;
import com.steamdistillery.respositories.AppRepository;
import com.steamdistillery.utils.SteamWebApi.SteamApp;
import com.steamdistillery.utils.SteamWebApi.SteamWebApiException;
import java.time.Duration;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateAppDatabase {
  private final SteamWebApi api;

  private final AppRepository repository;

  public UpdateAppDatabase(SteamWebApi api, AppRepository repository) {
    this.api = api;
    this.repository = repository;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void update() {
    log.info("Searching for new apps");

    var allApps = api.getApps();
    var knownAppIds = repository.getAppids();

    var numberOfNewApps = allApps.size() - knownAppIds.size();
    var timeToFinish = Duration.ofSeconds(numberOfNewApps * 2L)
        .toString()
        .substring(2)
        .replaceAll("(\\d[HMS])(?!$)", "$1 ");

    log.info("Adding {} new apps - ETA {}", numberOfNewApps, timeToFinish);

    allApps.stream()
        .filter(steamApp -> !knownAppIds.contains(steamApp.appid()))
        .map(this::processSteamApp)
        .filter(Objects::nonNull)
        .forEach(this::saveToDatabase);

    log.info("Finished searching for new apps");
  }

  private App processSteamApp(SteamApp steamApp) {
    log.info("Adding {}", steamApp);

    try {
      var response = api.getAppDetails(steamApp.appid());

      if (response.success()) {
        return response.app();
      } else {
        var removedApp = new App();
        removedApp.setAppid(steamApp.appid());
        removedApp.setName(steamApp.name());
        return removedApp;
      }
    } catch (SteamWebApiException e) {
      log.error("Failed to get details for appid:{}", steamApp);
      return null;
    }
  }

  private void saveToDatabase(App app) {
    try {
      repository.saveAndFlush(app);
    } catch (Exception e) {
      log.error("Failed to save [appid: " + app.getAppid() + "] to database", e);
    }
  }
}

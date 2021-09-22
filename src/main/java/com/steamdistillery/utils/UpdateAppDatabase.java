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

  private final AppRepository appRepository;

  public UpdateAppDatabase(SteamWebApi api, AppRepository appRepository) {
    this.api = api;
    this.appRepository = appRepository;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void update() {
    var allApps = api.getApps();
    var knownAppIds = appRepository.findAllAppIds();

    var numberOfApps = allApps.size();
    var numberOfKnownApps = knownAppIds.size();
    var numberOfNewApps = numberOfApps - numberOfKnownApps;
    var timeToFinish = Duration.ofSeconds(numberOfNewApps * 2L)
        .toString()
        .substring(2)
        .replaceAll("(\\d[HMS])(?!$)", "$1 ");

    log.info("{} total apps", numberOfApps);
    log.info("{} known apps already", numberOfKnownApps);
    log.info("Update will finish in {}", timeToFinish);

    allApps
        .stream()
        .filter(steamApp -> !knownAppIds.contains(steamApp.appid()))
        .map(this::processSteamApp)
        .filter(Objects::nonNull)
        .forEach(this::saveToDb);
  }

  private App processSteamApp(SteamApp steamApp) {
    log.info("Fetching details for {}", steamApp);

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

  private void saveToDb(App app) {
    log.info("Adding [appid={}, name={}] to database", app.getAppid(), app.getName());
    appRepository.save(app);
  }
}

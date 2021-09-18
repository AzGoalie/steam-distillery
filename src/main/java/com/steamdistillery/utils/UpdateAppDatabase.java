package com.steamdistillery.utils;

import com.steamdistillery.models.App;
import com.steamdistillery.respositories.AppRepository;
import com.steamdistillery.utils.SteamWebApi.SteamApp;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Service
public class UpdateAppDatabase {
  private final SteamWebApi steamAppRepository;

  private final AppRepository appRepository;

  public UpdateAppDatabase(SteamWebApi steamAppRepository, AppRepository appRepository) {
    this.steamAppRepository = steamAppRepository;
    this.appRepository = appRepository;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void update() {
    var allApps = steamAppRepository.getApps();
    log.info("{} total apps", allApps.size());
    log.info("{} known apps already", appRepository.count());

    allApps
        .stream()
        .filter(steamApp -> appRepository.findByAppid(steamApp.appid()) == null)
        .map(this::processSteamApp)
        .filter(Objects::nonNull)
        .forEach(this::saveToDb);
  }

  private App processSteamApp(SteamApp steamApp) {
    try {
      Thread.sleep(2000);
      log.info("Fetching details for {}", steamApp);
      var response = steamAppRepository.getAppDetails(steamApp.appid());

      if (response.success()) {
        return response.app();
      } else {
        var removedApp = new App();
        removedApp.setAppid(steamApp.appid());
        removedApp.setName(steamApp.name());
        return removedApp;
      }
    } catch (WebClientException e) {
      log.error("Network error when retrieving app details for {}", steamApp);
      return null;
    } catch (InterruptedException e) {
      log.error("Error while sleeping", e);
      return null;
    }
  }

  private void saveToDb(App app) {
    log.info("Adding [appid={}, name={}] to database", app.getAppid(), app.getName());
    appRepository.save(app);
  }
}

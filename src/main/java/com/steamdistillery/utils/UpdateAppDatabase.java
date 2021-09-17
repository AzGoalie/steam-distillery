package com.steamdistillery.utils;

import com.steamdistillery.models.App;
import com.steamdistillery.respositories.AppRepository;
import com.steamdistillery.respositories.SteamAppRepository;
import com.steamdistillery.respositories.SteamAppRepository.SteamApp;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateAppDatabase {
  private final SteamAppRepository steamAppRepository;

  private final AppRepository appRepository;

  public UpdateAppDatabase(SteamAppRepository steamAppRepository, AppRepository appRepository) {
    this.steamAppRepository = steamAppRepository;
    this.appRepository = appRepository;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60)
  public void update() {
    List<Integer> appids = appRepository.findAll().stream().map(App::getAppid).toList();
    log.info("{} known apps already", appids.size());

    steamAppRepository
        .getApps()
        .map(SteamApp::appid)
        .filter(appid -> !appids.contains(appid))
        .delayElements(Duration.ofSeconds(2))
        .doOnNext(appid -> log.info("Fetching details for appid:{}", appid))
        .flatMap(steamAppRepository::getAppDetails)
        .onErrorContinue((e, appid) -> log.error("Failed to get details for appid:}" + appid, e))
        .doOnNext(app -> log.info("Saving app [appid:{}, name:{}]", app.getAppid(), app.getName()))
        .subscribe(appRepository::save);
  }
}

package com.steam_distillery.job;

import com.steam_distillery.model.SteamApp;
import com.steam_distillery.repository.SteamAppRepository;
import com.steam_distillery.service.SteamApiService;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SteamAppUpdateJob {
  private static final Logger log = LoggerFactory.getLogger(SteamAppUpdateJob.class);
  private final SteamAppRepository repository;

  private final SteamApiService apiService;

  public SteamAppUpdateJob(SteamAppRepository repository, SteamApiService apiService) {
    this.repository = repository;
    this.apiService = apiService;
  }

  @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.HOURS)
  public void updateApps() {
    log.info("Updating steam app database");

    Set<Long> knownAppids = repository.findAllAppids();
    log.info("{} known apps", knownAppids.size());

    Set<Long> newAppids = apiService
        .getAllSteamAppids()
        .stream()
        .filter(appid -> !knownAppids.contains(appid))
        .collect(Collectors.toSet());
    log.info("{} new appids", newAppids.size());

    newAppids.forEach(appid -> apiService.getSteamApp(appid).ifPresent(this::addSteamApp));

    log.info("Finished updating steam app database");
  }

  private void addSteamApp(SteamApp app) {
    log.info("adding [{}]: {}", app.getAppid(), app.getName());
    repository.save(app);
  }
}

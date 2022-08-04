package com.steam_distillery.job;

import com.steam_distillery.model.SteamApp;
import com.steam_distillery.repository.SteamAppRepository;
import com.steam_distillery.service.SteamApiService;
import java.util.Optional;
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

    Set<SteamApp> newAppids = apiService
        .getAllSteamApps()
        .stream()
        .filter(app -> !knownAppids.contains(app.getAppid()))
        .collect(Collectors.toSet());
    log.info("{} new appids", newAppids.size());

    newAppids
        .stream()
        .map(this::fetchAppInfo)
        .flatMap(Optional::stream)
        .forEach(this::addSteamApp);

    log.info("Finished updating steam app database");
  }

  private Optional<SteamApp> fetchAppInfo(SteamApp app) {
    try {
      return apiService.getSteamApp(app.getAppid());
    } catch (Exception e) {
      log.error("Failed to get appinfo for appid: " + app.getAppid(), e);
      return Optional.empty();
    }
  }

  private void addSteamApp(SteamApp app) {
    log.debug("adding [{}]: {}", app.getAppid(), app.getName());
    repository.save(app);
  }
}

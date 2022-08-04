package com.steam_distillery.web;

import static com.steam_distillery.model.QSteamApp.steamApp;

import com.querydsl.core.BooleanBuilder;
import com.steam_distillery.model.Category;
import com.steam_distillery.model.SteamApp;
import com.steam_distillery.repository.SteamAppRepository;
import com.steam_distillery.service.SteamApiService;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class SteamAppResource {

  private static final Logger log = LoggerFactory.getLogger(SteamAppResource.class);
  private final SteamAppRepository appRepository;
  private final SteamApiService apiService;
  @Value("${max-page-size}")
  private int maxPageSize;

  public SteamAppResource(SteamAppRepository appRepository, SteamApiService apiService) {
    this.appRepository = appRepository;
    this.apiService = apiService;
  }

  @QueryMapping
  Page<SteamApp> apps(@Argument AppFilter filter) {
    if (filter == null) {
      log.info("Empty filter, returning first {} apps", maxPageSize);
      return appRepository.findAll(PageRequest.of(0, maxPageSize));
    }

    BooleanBuilder builder = new BooleanBuilder();
    if (isNotNullOrEmpty(filter.appids)) {
      builder.and(steamApp.appid.in(filter.appids));
    }

    if (isNotNullOrEmpty(filter.categories)) {
      builder.and(steamApp.categories.any().description.in(filter.categories));
    }

    Pageable page = PageRequest.of(filter.page, filter.limit);

    log.info("Fetching apps with filter: {} and page: {}", builder, page);
    return appRepository.findAll(builder, page);
  }

  @QueryMapping
  Optional<SteamApp> app(@Argument long appid) {
    log.info("Fetching app with id: {}", appid);
    return appRepository.findById(appid);
  }

  @QueryMapping
  Set<OwnedApps> getOwnedApps(@Argument List<Long> steamids) {
    log.info("Fetching owned games for steamids: {}", steamids);
    return steamids.stream().map(id -> new OwnedApps(id, apiService.getOwnedApps(id)))
        .collect(Collectors.toSet());
  }

  @BatchMapping(typeName = "App")
  Map<SteamApp, Set<String>> categories(Set<SteamApp> apps) {
    Set<Long> appids = apps.stream().map(SteamApp::getAppid).collect(Collectors.toSet());

    Map<SteamApp, Set<String>> result = new HashMap<>();
    appRepository.findAllByAppidIn(appids).forEach(app -> result.put(app,
        app.getCategories().stream().map(Category::getDescription).collect(Collectors.toSet())));

    return result;
  }

  @SchemaMapping(typeName = "AppPage")
  List<SteamApp> content(Page<SteamApp> page) {
    return page.getContent();
  }

  @SchemaMapping(typeName = "AppPage")
  Page<SteamApp> pageInfo(Page<SteamApp> page) {
    return page;
  }

  private boolean isNotNullOrEmpty(Collection<?> collection) {
    return Objects.nonNull(collection) && !collection.isEmpty();
  }

  private record AppFilter(List<Long> appids, List<String> categories, int limit, int page) {

  }

  record OwnedApps(long steamid, Set<SteamApp> apps) {

  }
}

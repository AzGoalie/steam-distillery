package com.steam_distillery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.steam_distillery.model.Category;
import com.steam_distillery.model.SteamApp;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SteamApiServiceTest {
  @Autowired
  private SteamApiService apiService;

  @Test
  public void testGetSteamApp() {
    final SteamApp halfLife = new SteamApp(70L, "Half-Life",
        Set.of(new Category(2L, "Single-player"), new Category(36L, "Online PvP")));

    Optional<SteamApp> result = apiService.getSteamApp(halfLife.getAppid());

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getAppid()).isEqualTo(halfLife.getAppid());
    assertThat(result.get().getName()).isEqualTo(halfLife.getName());
    assertThat(result.get().getCategories()).containsAll(halfLife.getCategories());
  }

  @Test
  public void testGetAllSteamApps() {
    Set<Long> results = apiService.getAllSteamAppids();
    assertThat(results).contains(10L);
    assertThat(results.size()).isGreaterThan(100000);
  }
}
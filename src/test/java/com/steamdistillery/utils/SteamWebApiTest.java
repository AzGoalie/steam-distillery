package com.steamdistillery.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class SteamWebApiTest {

  @Autowired
  private SteamWebApi repository;

  @Test
  public void testGetApps() {
    assertThat(repository.getApps()).isNotEmpty();
  }

  @Test
  public void testGetAppDetails() {
    assertThat(repository.getAppDetails(70)).isNotNull();
  }

}

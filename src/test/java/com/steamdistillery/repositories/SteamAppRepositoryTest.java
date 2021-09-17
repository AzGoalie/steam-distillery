package com.steamdistillery.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.steamdistillery.respositories.SteamAppRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SteamAppRepositoryTest {

  @Autowired
  private SteamAppRepository repository;

  @Test
  public void testGetApps() {
    assertThat(repository.getApps().collectList().block()).isNotEmpty();
  }

  @Test
  public void testGetAppDetails() {
    assertThat(repository.getAppDetails(70)).isNotNull();
  }

}

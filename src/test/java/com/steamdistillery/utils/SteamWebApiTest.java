package com.steamdistillery.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.steamdistillery.utils.SteamWebApi.SteamWebApiException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class SteamWebApiTest {

  @Autowired
  private SteamWebApi api;

  @Test
  public void testGetApps() {
    assertThat(api.getApps()).isNotEmpty();
  }

  @Test
  public void testGetAppDetails() throws SteamWebApiException {
    assertThat(api.getAppDetails(70)).isNotNull();
  }

  @Test
  public void testNullAppDetailsResponse() {
    assertThatThrownBy(() -> api.getAppDetails(1444140)).isInstanceOf(
        SteamWebApiException.class);
  }

}

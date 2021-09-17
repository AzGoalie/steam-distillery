package com.steamdistillery;

import com.steamdistillery.utils.UpdateAppDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SteamDistilleryApplicationTests {

  @Autowired
  private UpdateAppDatabase updateAppDatabase;

  @Test
  void contextLoads() {
    updateAppDatabase.update();
  }
}

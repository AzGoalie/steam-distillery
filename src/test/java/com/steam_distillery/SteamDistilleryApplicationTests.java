package com.steam_distillery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"steam.api-key=1234567890"})
class SteamDistilleryApplicationTests {

    @Test
    void contextLoads() {
    }

}

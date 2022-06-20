package com.steam_distillery;

import com.steam_distillery.model.SteamApp;
import com.steam_distillery.repository.SteamAppRepository;
import com.steam_distillery.service.SteamApiService;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class SteamDistilleryApplication {
  public static void main(String[] args) {
    SpringApplication.run(SteamDistilleryApplication.class, args);
  }

  @Bean
  @Profile("dev")
  CommandLineRunner initSteamApps(SteamAppRepository repository, SteamApiService apiService) {
    return args -> {
      SteamApp halfLife = apiService.getSteamApp(70L).get();
      SteamApp counterStrike = apiService.getSteamApp(10L).get();

      repository.saveAll(Set.of(halfLife, counterStrike));
    };
  }
}

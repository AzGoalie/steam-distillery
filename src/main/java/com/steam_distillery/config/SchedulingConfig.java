package com.steam_distillery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("!dev")
@Configuration
@EnableScheduling
public class SchedulingConfig {
}

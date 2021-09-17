package com.steamdistillery.configs;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class H2ConsoleConfig {
  private org.h2.tools.Server webServer;

  @Value("${h2-console-port}")
  Integer h2ConsolePort;

  @EventListener(ContextRefreshedEvent.class)
  public void start() throws SQLException {
    log.info("Starting h2 console at {}", h2ConsolePort);
    webServer = org.h2.tools.Server.createWebServer("-webPort", h2ConsolePort.toString(),
        "-tcpAllowOthers").start();
  }

  @EventListener(ContextClosedEvent.class)
  public void stop() {
    log.info("Stopping h2 console");
    webServer.stop();
  }
}

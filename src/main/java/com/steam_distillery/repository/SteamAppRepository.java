package com.steam_distillery.repository;

import com.steam_distillery.model.SteamApp;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;

public interface SteamAppRepository extends JpaRepository<SteamApp, Long>,
        QuerydslPredicateExecutor<SteamApp> {

    @EntityGraph("SteamApp.categories")
    List<SteamApp> findAllByAppidIn(Set<Long> appids);

    @Query("select app.appid from SteamApp app")
    Set<Long> findAllAppids();
}

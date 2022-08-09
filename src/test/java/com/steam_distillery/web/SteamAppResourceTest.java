package com.steam_distillery.web;

import com.steam_distillery.model.Category;
import com.steam_distillery.model.SteamApp;
import com.steam_distillery.repository.SteamAppRepository;
import com.steam_distillery.service.SteamApiService;
import com.steam_distillery.web.SteamAppResource.OwnedApps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Set;

import static org.mockito.Mockito.when;

@GraphQlTest(SteamAppResource.class)
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
public class SteamAppResourceTest {

    private static final SteamApp COUNTER_STRIKE = new SteamApp(10L, "Counter-Strike",
            Set.of(new Category(36L, "Online PvP")));
    private static final SteamApp HALF_LIFE = new SteamApp(70L, "Half-Life",
            Set.of(new Category(2L, "Single-player"), new Category(36L, "Online PvP")));

    @MockBean
    private SteamApiService apiService;

    @Autowired
    private GraphQlTester graphQlTester;

    @Autowired
    private SteamAppRepository appRepository;

    @BeforeEach
    public void setUp() {
        appRepository.saveAll(Set.of(COUNTER_STRIKE, HALF_LIFE));
    }

    @Test
    public void testGetAllApps() {
        graphQlTester
                .documentName("apps")
                .execute()
                .path("apps.content")
                .entityList(SteamApp.class)
                .hasSize(2)
                .contains(HALF_LIFE, COUNTER_STRIKE)
                .path("apps.content[0].categories")
                .entityList(String.class)
                .contains("Online PvP");
    }

    @Test
    public void testGetSpecificApp() {
        graphQlTester
                .documentName("app")
                .variable("appid", "10")
                .execute()
                .path("app")
                .entity(SteamApp.class)
                .isEqualTo(COUNTER_STRIKE);
    }

    @Test
    public void testGetOwnedApps() {
        final long steamid1 = 1L;
        final long steamid2 = 2L;

        final Set<SteamApp> steamid1Apps = Set.of(HALF_LIFE);
        final Set<SteamApp> steamid2Apps = Set.of(COUNTER_STRIKE);

        when(apiService.getOwnedApps(steamid1)).thenReturn(steamid1Apps);
        when(apiService.getOwnedApps(steamid2)).thenReturn(steamid2Apps);

        final OwnedApps steamid1Result = new OwnedApps(steamid1, steamid1Apps);
        final OwnedApps steamid2Result = new OwnedApps(steamid2, steamid2Apps);

        graphQlTester
                .documentName("getOwnedApps")
                .variable("steamids", Set.of(steamid1, steamid2))
                .execute()
                .path("getOwnedApps")
                .entityList(OwnedApps.class)
                .contains(steamid1Result)
                .contains(steamid2Result);
    }
}

package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.property.PropertyMapping;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ConfigurableModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;

public interface AnnihilationAPI {

    UtilityPack utilities();

    PlayerRegistry playerCache();

    GameService gameService();

    ConfigurableModelService modelService();

    PropertyMapping getPropertyMapping();

    Scheduler getScheduler();

    LocationReference getLobbySpawn();

    void setLobbySpawn(LocationReference lobbySpawn);

}
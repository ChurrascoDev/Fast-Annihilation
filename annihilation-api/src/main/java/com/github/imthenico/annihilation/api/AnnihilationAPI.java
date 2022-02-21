package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.cache.ConfigurableModelCache;
import com.github.imthenico.annihilation.api.game.AnnihilationGameFactory;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.property.PropertyMapping;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ConfigurableModelService;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;

public interface AnnihilationAPI {

    UtilityPack utilities();

    PlayerRegistry playerCache();

    GameInstanceManager gameRegistry();

    AnnihilationGameFactory gameFactory();

    ConfigurableModelService modelService();

    PropertyMapping getPropertyMapping();

    Scheduler getScheduler();

    LocationReference getLobbySpawn();

    void setLobbySpawn(LocationReference lobbySpawn);

}
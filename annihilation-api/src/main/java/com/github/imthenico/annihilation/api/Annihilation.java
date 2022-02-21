package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.game.AnnihilationGameFactory;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.map.ConfigurableModelManager;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;

public interface Annihilation {

    UtilityPack utilities();

    PlayerRegistry playerCache();

    GameInstanceManager gameRegistry();

    AnnihilationGameFactory gameFactory();

    ConfigurableModelManager getMapManager();

    Scheduler getScheduler();

    LocationReference getLobbySpawn();

    void setLobbySpawn(LocationReference lobbySpawn);

}
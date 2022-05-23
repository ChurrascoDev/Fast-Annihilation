package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.registry.ModelTypeRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import com.github.imthenico.annihilation.api.util.UtilityPack;

public interface AnnihilationAPI {

    UtilityPack utilities();

    PlayerRegistry playerCache();

    GameService gameService();

    ModelService modelService();

    ScoreboardService scoreboardService();

    Scheduler getScheduler();

    PluginHandler getPluginHandler();

    ModelTypeRegistry getModelTypeRegistry();

}
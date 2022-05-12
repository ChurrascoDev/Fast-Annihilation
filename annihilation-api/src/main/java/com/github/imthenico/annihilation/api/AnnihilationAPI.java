package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.registry.ModelTypeRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.inject.InjectionHandler;

public interface AnnihilationAPI {

    InjectionHandler INJECTION_HANDLER = InjectionHandler.create();

    UtilityPack utilities();

    PlayerRegistry playerCache();

    GameService gameService();

    ModelService modelService();

    Scheduler getScheduler();

    PluginHandler getPluginHandler();

    ModelTypeRegistry getModelTypeRegistry();

}
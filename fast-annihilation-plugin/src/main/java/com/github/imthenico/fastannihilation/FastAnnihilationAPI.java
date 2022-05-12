package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.registry.ModelTypeRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.util.UtilityPack;

public class FastAnnihilationAPI implements AnnihilationAPI {

    private final UtilityPack utilityPack;
    private final PlayerRegistry playerRegistry;
    private final FastAnnihilationPlugin fastAnnihilationPlugin;
    private final Scheduler scheduler;
    private final PluginHandler pluginHandler;
    private final ModelTypeRegistry modelTypeRegistry = new ModelTypeRegistry();

    public FastAnnihilationAPI(
            UtilityPack utilityPack,
            PlayerRegistry playerRegistry,
            FastAnnihilationPlugin fastAnnihilationPlugin,
            Scheduler scheduler,
            PluginHandler pluginHandler
    ) {
        this.utilityPack = utilityPack;
        this.playerRegistry = playerRegistry;
        this.fastAnnihilationPlugin = fastAnnihilationPlugin;
        this.scheduler = scheduler;
        this.pluginHandler = pluginHandler;
    }

    @Override
    public UtilityPack utilities() {
        return utilityPack;
    }

    @Override
    public PlayerRegistry playerCache() {
        return playerRegistry;
    }

    @Override
    public GameService gameService() {
        return fastAnnihilationPlugin.getGameService();
    }

    @Override
    public ModelService modelService() {
        return fastAnnihilationPlugin.getModelService();
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public PluginHandler getPluginHandler() {
        return pluginHandler;
    }

    @Override
    public ModelTypeRegistry getModelTypeRegistry() {
        return modelTypeRegistry;
    }
}
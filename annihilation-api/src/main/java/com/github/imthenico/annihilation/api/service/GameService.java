package com.github.imthenico.annihilation.api.service;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.game.GameFactory;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.game.SimpleGameInstanceManager;
import com.github.imthenico.annihilation.api.game.SimpleGameRoomFactory;
import com.github.imthenico.simplecommons.bukkit.service.AbstractPluginService;

public class GameService extends AbstractPluginService {

    private final GameInstanceManager gameInstanceManager;
    private final GameFactory gameFactory;

    public GameService(
            AnnihilationAPI api
    ) {
        this.gameInstanceManager = new SimpleGameInstanceManager();
        this.gameFactory = new SimpleGameRoomFactory(api, gameInstanceManager);
    }

    @Override
    protected void onEnd() {

    }

    @Override
    protected void onStart() {

    }

    public GameInstanceManager gameManager() {
        return gameInstanceManager;
    }

    public GameFactory factory() {
        return gameFactory;
    }
}
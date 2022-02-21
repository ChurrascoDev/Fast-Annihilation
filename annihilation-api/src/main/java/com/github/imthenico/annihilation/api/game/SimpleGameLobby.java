package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.model.SimpleLoadedModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.world.SimpleWorld;

import java.util.Map;

public class SimpleGameLobby extends SimpleLoadedModel implements GameLobby {

    private final String gameId;
    private final GameInstanceManager gameInstanceManager;

    public SimpleGameLobby(
            SimpleWorld mainWorld,
            Map<String, SimpleWorld> worlds,
            ConfigurableModel source,
            PropertiesContainer propertiesContainer,
            String gameId,
            GameInstanceManager gameInstanceManager
    ) {
        super(mainWorld, worlds, source, propertiesContainer);
        this.gameId = gameId;
        this.gameInstanceManager = gameInstanceManager;
    }

    @Override
    public GameInstance getGame() {
        return gameInstanceManager.getGame(gameId);
    }
}
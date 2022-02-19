package com.github.imthenico.annihilation.api.converter;

import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.game.GameLobby;
import com.github.imthenico.annihilation.api.game.SimpleGameLobby;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.world.SimpleWorld;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Map;

public class ConverterToGameLobby extends AbstractConverter<GameLobby> {

    private final GameInstanceManager gameInstanceManager;

    public ConverterToGameLobby(GameInstanceManager gameInstanceManager) {
        this.gameInstanceManager = gameInstanceManager;
    }

    @Override
    protected String[] getRequiredTags() {
        return new String[] {"lobby-model"};
    }

    @Override
    protected GameLobby create(
            ConfigurableModel model,
            Map<String, SimpleWorld> worlds,
            PropertiesContainer propertiesContainer,
            Map<String, Object> extraData
    ) {
        String gameId = Validate.isTrue(
                extraData.containsKey("gameId"),
                "No game id provided",
                (String) extraData.get("gameId")
        );

        return new SimpleGameLobby(
                worlds.get("main"),
                worlds,
                model,
                propertiesContainer,
                gameId,
                gameInstanceManager
        );
    }
}
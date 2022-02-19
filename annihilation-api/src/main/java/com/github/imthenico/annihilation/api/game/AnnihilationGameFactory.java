package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.world.SimpleWorld;

import java.util.Map;

public interface AnnihilationGameFactory {

    GameInstance newGame(
            String matchType,
            ConfigurableModel model,
            Map<String, String> extraData
    ) throws IllegalArgumentException;

    void registerMatchCreator(
            String matchTypeName,
            MatchFactory matchFactory
    ) throws IllegalArgumentException;

    void setToLobbyConverter(ModelConverter<GameLobby> converter);
}
package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;

public interface AnnihilationGameFactory {

    GameInstance newGame(
            String id,
            String matchType,
            ConfigurableModel lobbyModel
    ) throws IllegalArgumentException;

    void registerMatchCreator(
            String matchTypeName,
            MatchFactory matchFactory
    ) throws IllegalArgumentException;

    void setToLobbyConverter(ModelConverter<GameLobby> converter);
}
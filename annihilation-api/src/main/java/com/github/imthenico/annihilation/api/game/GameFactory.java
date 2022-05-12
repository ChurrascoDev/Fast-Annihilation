package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.gmlib.MapModel;

public interface GameFactory {

    GameRoom newGame(
            String id,
            String typeName,
            MapModel<? extends GameLobbyData> lobbyModel
    ) throws IllegalArgumentException;

    GameExpansion getConfiguration(String typeName);

    void registerExpansion(String typeName, GameExpansion gameExpansion);

}
package com.github.imthenico.fastannihilation.model.lobby;

import com.github.imthenico.annihilation.api.model.lobby.GameLobby;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.gmlib.CustomMapFactory;
import com.github.imthenico.gmlib.metadata.MetadataSnapshot;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameLobbyFactory implements CustomMapFactory<GameLobbyData, GameLobby> {
    @Override
    public @Nullable GameLobby createMap(
            @NotNull AWorld aWorld,
            @NotNull WorldContainer<AWorld> worldContainer,
            @NotNull String s,
            @NotNull GameLobbyData gameLobbyData,
            @NotNull MetadataSnapshot metadataSnapshot
    ) {
        return new GameLobby(gameLobbyData);
    }
}
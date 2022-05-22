package com.github.imthenico.fastannihilation.model;

import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.fastannihilation.model.lobby.GameLobbyDeserializer;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.fastannihilation.model.map.MatchMapDataDeserializer;
import com.github.imthenico.fastannihilation.model.data.GsonDataSerializer;
import com.github.imthenico.gmlib.handler.DataManipulationModule;
import com.github.imthenico.gmlib.handler.HandlerRegistry;
import com.google.gson.Gson;

public class AnniModule implements DataManipulationModule {

    private final Gson gson;

    public AnniModule(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void configure(HandlerRegistry.Builder builder) {
        builder.deserializer(MatchMapData.class, new MatchMapDataDeserializer(gson));
        builder.serializer(MatchMapData.class, new GsonDataSerializer<>(gson));

        builder.deserializer(GameLobbyData.class, new GameLobbyDeserializer(gson));
        builder.serializer(GameLobbyData.class, new GsonDataSerializer<>(gson));
    }
}
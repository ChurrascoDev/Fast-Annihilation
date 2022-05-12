package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyDeserializer;
import com.github.imthenico.annihilation.api.model.map.data.MatchMapData;
import com.github.imthenico.annihilation.api.model.map.data.MatchMapDataDeserializer;
import com.github.imthenico.annihilation.api.model.map.data.GsonDataSerializer;
import com.github.imthenico.gmlib.DataManipulation;
import com.github.imthenico.gmlib.DataManipulationModule;
import com.google.gson.Gson;

public class AnniModule implements DataManipulationModule {

    private final Gson gson;

    public AnniModule(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void configure(DataManipulation.Builder builder) {
        builder.deserializer(MatchMapData.class, new MatchMapDataDeserializer(gson));
        builder.serializer(MatchMapData.class, new GsonDataSerializer<>(gson));

        builder.deserializer(GameLobbyData.class, new GameLobbyDeserializer());
        builder.serializer(GameLobbyData.class, new GsonDataSerializer<>(gson));
    }
}
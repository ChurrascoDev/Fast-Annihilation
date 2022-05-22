package com.github.imthenico.fastannihilation.model.lobby;

import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.gmlib.handler.DataDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GameLobbyDeserializer implements DataDeserializer<GameLobbyData> {

    private final Gson gson;

    public GameLobbyDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public GameLobbyData deserializeData(JsonObject jsonObject) {
        return gson.fromJson(jsonObject, GameLobbyData.class);
    }
}
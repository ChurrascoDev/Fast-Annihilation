package com.github.imthenico.annihilation.api.model.lobby;

import com.github.imthenico.gmlib.DataDeserializer;
import com.github.imthenico.json.JsonReader;

public class GameLobbyDeserializer implements DataDeserializer<GameLobbyData> {
    @Override
    public GameLobbyData deserializeData(JsonReader jsonReader) {
        return jsonReader.mapTo(GameLobbyData.class);
    }
}
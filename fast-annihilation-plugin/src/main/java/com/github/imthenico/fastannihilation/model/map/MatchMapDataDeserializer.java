package com.github.imthenico.fastannihilation.model.map;

import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.handler.DataDeserializer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MatchMapDataDeserializer implements DataDeserializer<MatchMapData> {

    private final Gson gson;

    public MatchMapDataDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public MatchMapData deserializeData(JsonObject jsonObject) {
        return gson.fromJson(jsonObject, MatchMapData.class);
    }
}
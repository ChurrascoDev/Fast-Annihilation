package com.github.imthenico.fastannihilation.model.map;

import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.DataDeserializer;
import com.github.imthenico.json.JsonReader;
import com.google.gson.Gson;

public class MatchMapDataDeserializer implements DataDeserializer<MatchMapData> {

    private final Gson gson;

    public MatchMapDataDeserializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public MatchMapData deserializeData(JsonReader jsonReader) {
        jsonReader = jsonReader.copy(gson);

        return jsonReader.mapTo(MatchMapData.class);
    }
}
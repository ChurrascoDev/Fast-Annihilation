package com.github.imthenico.fastannihilation.model.data;

import com.github.imthenico.gmlib.DataSerializer;
import com.github.imthenico.gmlib.ModelData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonDataSerializer<T extends ModelData> implements DataSerializer<T> {

    private final Gson gson;

    public GsonDataSerializer(Gson gson) {
        this.gson = gson;
    }

    @Override
    public JsonObject serializeData(T mapData) {
        return gson.toJsonTree(mapData).getAsJsonObject();
    }
}
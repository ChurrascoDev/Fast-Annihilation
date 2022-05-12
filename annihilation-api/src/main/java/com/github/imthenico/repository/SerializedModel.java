package com.github.imthenico.repository;

import com.google.gson.JsonObject;

import java.util.Objects;

public class SerializedModel {

    private final JsonObject jsonObject;
    private final String jsonInText;

    SerializedModel(JsonObject jsonObject, String jsonInText) {
        this.jsonObject = jsonObject;
        this.jsonInText = jsonInText;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public String getJsonInText() {
        return jsonInText;
    }

    public static SerializedModel of(
            JsonObject jsonObject,
            String jsonText
    ) {
        return new SerializedModel(
                Objects.requireNonNull(jsonObject),
                Objects.requireNonNull(jsonText)
        );
    }
}
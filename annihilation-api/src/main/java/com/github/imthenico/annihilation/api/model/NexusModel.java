package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.json.JsonSerializable;
import com.github.imthenico.json.JsonTreeBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public class NexusModel implements JsonSerializable {

    public final int totalHealth;
    private final LocationModel location;

    public NexusModel(
            int totalHealth,
            LocationModel location
    ) {
        this.totalHealth = totalHealth;
        this.location = location.copy();
    }

    public LocationModel getLocation() {
        return location.copy();
    }

    public NexusModel copy() {
        return new NexusModel(totalHealth, location.copy());
    }

    @Override
    public @NotNull JsonElement serialize() {
        return JsonTreeBuilder.create()
                .add("totalHealth", totalHealth)
                .add("location", location)
                .build();
    }
}
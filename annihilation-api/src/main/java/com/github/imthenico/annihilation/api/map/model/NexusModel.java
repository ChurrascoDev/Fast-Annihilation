package com.github.imthenico.annihilation.api.map.model;

import com.github.imthenico.annihilation.api.property.serialization.SerializableValue;
import com.github.imthenico.simplecommons.minecraft.LocationModel;

import java.util.HashMap;
import java.util.Map;

public class NexusModel implements SerializableValue {

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

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("location", location.serialize());
        objectMap.put("health", totalHealth);

        return objectMap;
    }
}
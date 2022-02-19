package com.github.imthenico.annihilation.api.map.model;

import com.github.imthenico.annihilation.api.property.serialization.SerializableValue;
import com.github.imthenico.simplecommons.minecraft.LocationModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class BossModel implements SerializableValue {

    public final int totalHealth;
    public final String typeName;
    public final String displayName;
    public final String entityTypeName;
    public final String id;
    private final LocationModel spawnLocation;

    public BossModel(
            int totalHealth,
            String typeName,
            String displayName,
            String entityTypeName,
            String id,
            LocationModel spawnLocation
    ) {
        this.totalHealth = totalHealth;
        this.typeName = typeName;
        this.displayName = displayName;
        this.entityTypeName = entityTypeName;
        this.id = id;
        this.spawnLocation = spawnLocation;
    }

    public LocationModel getSpawnLocation() {
        return spawnLocation.copy();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        objectMap.put("totalHealth", totalHealth);
        objectMap.put("displayName", displayName);
        objectMap.put("entityTypeName", entityTypeName);
        objectMap.put("id", id);
        objectMap.put("spawnLocation", spawnLocation.serialize());

        return objectMap;
    }
}
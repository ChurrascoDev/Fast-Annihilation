package com.github.imthenico.annihilation.api.config;

import com.github.imthenico.annihilation.api.db.UserCredential;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnniConfig implements ConfigurationSerializable {

    private final String swmLoaderName;
    private final String storageSourceTypeName;
    private final UserCredential userStorageCredential;

    public AnniConfig(
            String swmLoaderName,
            String storageSourceTypeName,
            UserCredential userStorageCredential
    ) {
        this.swmLoaderName = swmLoaderName;
        this.storageSourceTypeName = storageSourceTypeName;
        this.userStorageCredential = userStorageCredential;
    }

    public String getSwmLoaderName() {
        return swmLoaderName;
    }

    public String getStorageSourceTypeName() {
        return storageSourceTypeName;
    }

    public UserCredential getUserStorageCredential() {
        return userStorageCredential;
    }

    public AnniConfig copy() {
        return new AnniConfig(
                swmLoaderName,
                storageSourceTypeName,
                userStorageCredential
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnniConfig that = (AnniConfig) o;
        return swmLoaderName.equals(that.swmLoaderName) && Objects.equals(storageSourceTypeName, that.storageSourceTypeName) && Objects.equals(userStorageCredential, that.userStorageCredential);
    }

    @Override
    public int hashCode() {
        return Objects.hash(swmLoaderName, storageSourceTypeName, userStorageCredential);
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> objectMap = new HashMap<>();

        objectMap.put("swmLoaderName", swmLoaderName);
        objectMap.put("storageSourceTypeName", storageSourceTypeName);
        objectMap.put("userStorageCredential", userStorageCredential);

        return objectMap;
    }

    @SuppressWarnings("unchecked")
    public static AnniConfig deserialize(Map<String, Object> objectMap) {
        return new AnniConfig(
                (String) objectMap.get("swmLoaderName"),
                (String) objectMap.get("storageSourceTypeName"),
                UserCredential.deserialize((Map<String, Object>) objectMap.get("userStorageCredential"))
        );
    }
}
package com.github.imthenico.annihilation.api.registry;

import com.github.imthenico.gmlib.ModelData;

import java.util.HashMap;
import java.util.Map;

public class ModelTypeRegistry {

    private final Map<String, ModelType<?>> typesByName;

    public ModelTypeRegistry() {
        this.typesByName = new HashMap<>();
    }

    public void register(String name, ModelType<?> modelType) {
        if (typesByName.containsKey(name) || typesByName.containsValue(modelType)) {
            throw new IllegalArgumentException("Key or Value is already registered");
        }

        this.typesByName.put(name, modelType);
    }

    public ModelType<?> get(String name) {
        return typesByName.get(name);
    }

    public String getName(Class<? extends ModelData> clazz) {
        for (Map.Entry<String, ModelType<?>> entry : typesByName.entrySet()) {
            if (clazz.equals(entry.getValue().getClazz()))
                return entry.getKey();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends ModelData> ModelType<T> getOrThrow(String name, Class<T> expectedClass) {
        ModelType<?> modelType = get(name);

        if (modelType == null)
            return null;

        if (!modelType.getClazz().equals(expectedClass))
            throw new IllegalArgumentException("Class is not of type expected");

        return (ModelType<T>) modelType;
    }

    public Map<String, ModelType<?>> getAll() {
        return new HashMap<>(typesByName);
    }

}
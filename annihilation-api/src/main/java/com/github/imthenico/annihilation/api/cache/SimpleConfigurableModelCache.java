package com.github.imthenico.annihilation.api.cache;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleConfigurableModelCache implements ConfigurableModelCache {

    private final Map<String, ConfigurableModel> models;

    public SimpleConfigurableModelCache() {
        this.models = new HashMap<>();
    }

    @Override
    public void addModel(ConfigurableModel model) {
        models.put(model.getId(), model);
    }

    @Override
    public ConfigurableModel removeModel(String name) {
        return models.remove(name);
    }

    @Override
    public ConfigurableModel getModel(String name) {
        return models.get(name);
    }

    @Override
    public Map<String, ConfigurableModel> getModels() {
        return Collections.unmodifiableMap(models);
    }

    @Override
    public void clear() {
        models.clear();
    }

    @Override
    public boolean has(String name) {
        return models.containsKey(name);
    }
}
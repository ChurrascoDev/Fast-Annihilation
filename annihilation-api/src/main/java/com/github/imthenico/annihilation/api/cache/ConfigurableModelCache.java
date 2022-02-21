package com.github.imthenico.annihilation.api.cache;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;

import java.util.Map;

public interface ConfigurableModelCache {

    void addModel(ConfigurableModel model);

    ConfigurableModel removeModel(String name);

    ConfigurableModel getModel(String name);

    Map<String, ConfigurableModel> getModels();

    void clear();

    boolean has(String name);

}
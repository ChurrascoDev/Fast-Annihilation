package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.SimplePropertiesContainer;
import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleConfigurableModel implements ConfigurableModel {

    private final String id;
    private final LoadedWorldTemplate mainWorld;
    private final Map<String, LoadedWorldTemplate> worlds;
    private final PropertiesContainer propertiesContainer;

    public SimpleConfigurableModel(
            String id,
            LoadedWorldTemplate mainWorld,
            Map<String, LoadedWorldTemplate> worlds,
            PropertiesContainer propertiesContainer
    ) {
        this.id = id;
        this.mainWorld = mainWorld;
        this.worlds = worlds;
        this.propertiesContainer = propertiesContainer;
    }

    public SimpleConfigurableModel(String id, LoadedWorldTemplate mainWorld) {
        this(id, mainWorld, new ConcurrentHashMap<>(), new SimplePropertiesContainer());
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public LoadedWorldTemplate getMainWorld() {
        return mainWorld;
    }

    @Override
    public Map<String, LoadedWorldTemplate> getWorlds() {
        return worlds;
    }

    @Override
    public void addWorld(LoadedWorldTemplate worldTemplate) {
        this.worlds.put(worldTemplate.getSource().getName(), worldTemplate);
    }

    @Override
    public boolean removeWorld(String worldName) {
        return this.worlds.remove(worldName) != null;
    }

    @Override
    public PropertiesContainer getProperties() {
        return propertiesContainer;
    }
}
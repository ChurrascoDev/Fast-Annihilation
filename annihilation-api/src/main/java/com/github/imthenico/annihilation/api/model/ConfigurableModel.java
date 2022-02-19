package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.property.PropertyHolder;
import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;

import java.util.Map;

public interface ConfigurableModel extends PropertyHolder {

    String getId();

    LoadedWorldTemplate getMainWorld();

    Map<String, LoadedWorldTemplate> getWorlds();

    void addWorld(LoadedWorldTemplate worldTemplate);

    boolean removeWorld(String worldName);
}
package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.property.PropertyHolder;
import com.github.imthenico.annihilation.api.world.SimpleWorld;

import java.util.Map;

public interface LoadedModel extends PropertyHolder {

    SimpleWorld getMainWorld();

    Map<String, SimpleWorld> getWorlds();

    SimpleWorld getWorld(String name);

    ConfigurableModel getSource();

}
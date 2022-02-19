package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.world.SimpleWorld;
import org.bukkit.Bukkit;

import java.util.Map;

public class SimpleLoadedModel implements LoadedModel {

    protected final SimpleWorld mainWorld;
    protected final Map<String, SimpleWorld> worlds;
    protected final PropertiesContainer propertiesContainer;
    protected final ConfigurableModel source;

    protected SimpleLoadedModel(
            SimpleWorld mainWorld,
            Map<String, SimpleWorld> worlds,
            ConfigurableModel source,
            PropertiesContainer propertiesContainer
    ) {
        this.mainWorld = mainWorld;
        this.worlds = worlds;
        this.propertiesContainer = propertiesContainer;
        this.source = source;
    }

    @Override
    public SimpleWorld getMainWorld() {
        String internalName = mainWorld.getWorld().getName();

        if (Bukkit.getWorld(internalName) == null)
            return null;

        return mainWorld;
    }

    @Override
    public Map<String, SimpleWorld> getWorlds() {
        return worlds;
    }

    @Override
    public SimpleWorld getWorld(String name) {
        SimpleWorld world = worlds.get(name);

        if (world == null)
            return null;

        String internalName = world.getWorld().getName();

        if (Bukkit.getWorld(internalName) == null) return null;

        return world;
    }

    @Override
    public ConfigurableModel getSource() {
        return source;
    }

    @Override
    public PropertiesContainer getProperties() {
        return propertiesContainer;
    }
}
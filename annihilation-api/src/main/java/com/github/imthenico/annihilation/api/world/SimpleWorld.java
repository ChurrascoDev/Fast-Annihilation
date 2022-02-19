package com.github.imthenico.annihilation.api.world;

import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.World;

public class SimpleWorld {

    private final World world;
    private final LocationReference spawnLocationReference;
    private final String name;
    private final WorldTemplate template;

    public SimpleWorld(
            World world,
            String name,
            WorldTemplate template
    ) {
        this.world = Validate.notNull(world);
        this.spawnLocationReference = world::getSpawnLocation;
        this.name = Validate.notNull(name);
        this.template = template;
    }

    public World getWorld() {
        return world;
    }

    public LocationReference getSpawnLocation() {
        return spawnLocationReference;
    }

    public String getName() {
        return name;
    }

    public WorldTemplate getTemplate() {
        return template;
    }

    public static SimpleWorld generate(WorldTemplate worldTemplate, String name) {
        return new SimpleWorld(worldTemplate.generateNewWorld(name), name, worldTemplate);
    }
}
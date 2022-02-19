package com.github.imthenico.annihilation.api.world;

import org.bukkit.World;

public interface LoadedWorldTemplate extends WorldTemplate {

    World getWorld();

    void save();

}
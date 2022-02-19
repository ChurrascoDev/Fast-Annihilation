package com.github.imthenico.annihilation.api.world;

import com.github.imthenico.simplecommons.minecraft.LocationModel;
import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.World;

public interface WorldTemplate {

    World generateNewWorld(String newName);

    SlimeWorld getSource();

    LocationModel getSpawnLocation();

}
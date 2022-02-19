package com.github.imthenico.fastannihilation.template;

import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;
import com.github.imthenico.simplecommons.minecraft.LocationModel;
import com.github.imthenico.simplecommons.util.Validate;
import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

public class LoadedSlimeWorldTemplate implements LoadedWorldTemplate {

    private final UUID worldId;
    private final SlimeWorldTemplate original;

    public LoadedSlimeWorldTemplate(World world, SlimeWorldTemplate original) {
        this.original = Validate.notNull(original);

        Validate.isTrue(world.getName().equals(getSource().getName()));
        this.worldId = world.getUID();
    }

    @Override
    public World getWorld() {
        return Bukkit.getWorld(worldId);
    }

    @Override
    public void save() {
        getWorld().save();
    }

    @Override
    public World generateNewWorld(String newName) {
        return original.generateNewWorld(newName);
    }

    @Override
    public SlimeWorld getSource() {
        return original.getSource();
    }

    @Override
    public LocationModel getSpawnLocation() {
        return original.getSpawnLocation();
    }
}
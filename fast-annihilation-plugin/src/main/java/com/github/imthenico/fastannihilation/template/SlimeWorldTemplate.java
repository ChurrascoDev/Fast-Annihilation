package com.github.imthenico.fastannihilation.template;

import com.github.imthenico.annihilation.api.world.SimpleWorld;
import com.github.imthenico.annihilation.api.world.WorldTemplate;
import com.github.imthenico.simplecommons.minecraft.LocationModel;
import com.github.imthenico.simplecommons.util.Validate;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class SlimeWorldTemplate implements WorldTemplate {

    private final SlimeWorld source;
    private final SlimePlugin slimePlugin;

    public SlimeWorldTemplate(SlimeWorld source) {
        this.source = Validate.notNull(source);
        this.slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
    }

    @Override
    public World generateNewWorld(String newName) {
        Validate.isTrue(Bukkit.getWorld(newName) == null, "This world name is already occupied");

        slimePlugin.generateWorld(source.clone(newName));

        return Bukkit.getWorld(newName);
    }

    @Override
    public SlimeWorld getSource() {
        return source;
    }

    @Override
    public LocationModel getSpawnLocation() {
        SlimeWorld.SlimeProperties slimeProperties = source.getProperties();

        double x = slimeProperties.getSpawnX();
        double y = slimeProperties.getSpawnY();
        double z = slimeProperties.getSpawnZ();

        return new LocationModel(x, y, z, source.getName());
    }
}
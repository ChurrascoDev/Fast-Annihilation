package com.github.imthenico.fastannihilation.template;

import com.github.imthenico.annihilation.api.provider.WorldTemplateLoader;
import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;
import com.github.imthenico.annihilation.api.world.WorldTemplate;
import com.github.imthenico.simplecommons.util.Validate;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;
import java.util.*;

public class SlimeWorldTemplateLoader implements WorldTemplateLoader {

    private final SlimePlugin plugin;
    private final SlimeLoader loader;
    private final Map<String, SlimeWorld> cachedWorlds;

    public SlimeWorldTemplateLoader(SlimeLoader loader) {
        this.loader = Validate.notNull(loader);
        this.plugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        this.cachedWorlds = new HashMap<>();
    }

    @Override
    public Optional<LoadedWorldTemplate> newTemplate(String name) {
        try {
            SlimeWorld slimeWorld = cachedWorlds.get(name);

            if (slimeWorld == null) {
                slimeWorld = plugin.loadWorld(loader, name, false, new SlimePropertyMap());
                cachedWorlds.put(name, slimeWorld);
            }

            SlimeWorldTemplate worldTemplate = new SlimeWorldTemplate(slimeWorld);

            World world = Bukkit.getWorld(name);

            if (world == null)
                world = worldTemplate.generateNewWorld(name);

            return Optional.of(new LoadedSlimeWorldTemplate(world, worldTemplate));
        } catch (UnknownWorldException ignored) {
        } catch (WorldInUseException | IOException | CorruptedWorldException | NewerFormatException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Set<LoadedWorldTemplate> getAllTemplates() {
        try {
            Set<LoadedWorldTemplate> templates = new HashSet<>();

            for (String worldName : loader.listWorlds()) {
                newTemplate(worldName).ifPresent(templates::add);
            }

            return templates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
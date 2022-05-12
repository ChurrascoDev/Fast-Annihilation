package com.github.imthenico.fastannihilation.service;

import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.service.Service;
import com.github.imthenico.annihilation.api.util.CompletableFutures;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.fastannihilation.storage.AtomicAnniStorage;
import com.github.imthenico.fastannihilation.storage.StorageHandlerProviderFactory;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.logging.Level;

public class PluginStorageService implements Service {

    private final PluginHandler pluginHandler;
    private final Gson gson;
    private final JavaPlugin javaPlugin;
    private final AtomicAnniStorage atomicAnniStorage = new AtomicAnniStorage();

    public PluginStorageService(PluginHandler pluginHandler, Gson gson, JavaPlugin javaPlugin) {
        this.pluginHandler = pluginHandler;
        this.gson = gson;
        this.javaPlugin = javaPlugin;
    }

    @Override
    public void start() {
        loadStorage();
    }

    @Override
    public void end() {

    }

    public void loadStorage() {
        try {
            Bukkit.getLogger().info("Loading Annihilation storage...");

            pluginHandler.reloadConfig();

            AnniConfig anniConfig = pluginHandler.getPluginConfig();

            AnniStorage anniStorage = StorageHandlerProviderFactory
                    .of(gson, pluginHandler.getPluginConfig().getStorageSourceTypeName())
                    .createHandler(javaPlugin, anniConfig);

            atomicAnniStorage.consume(anniStorage);

            Bukkit.getLogger().info("Annihilation storage loaded successfully");
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to load plugin storage after, disabling...", e);
            Bukkit.getPluginManager().disablePlugin(javaPlugin);
        }
    }

    public AtomicAnniStorage getAnniStorage() {
        return atomicAnniStorage;
    }
}
package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.storage.AnniStorage;
import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.function.Supplier;

public interface PluginHandler {

    File getFolder();

    Configuration getYaml();

    AnniConfig getPluginConfig();

    Supplier<AnniStorage> getStorageHandler();

    void reloadConfig();

    void reloadStorage();

    void reloadMaps();

    void reloadMessages();

    void reloadAll();

    <T extends JavaPlugin> T getPlugin();

}
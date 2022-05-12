package com.github.imthenico.annihilation.api;

import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.config.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public interface PluginHandler {

    File getFolder();

    Configuration getYaml();

    AnniConfig getPluginConfig();

    void reloadConfig();

    void reloadMessages();

    void reloadAll();

    <T extends JavaPlugin> T getPlugin();

}
package com.github.imthenico.fastannihilation;

import com.github.imthenico.fastannihilation.config.AnniConfig;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;

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

}
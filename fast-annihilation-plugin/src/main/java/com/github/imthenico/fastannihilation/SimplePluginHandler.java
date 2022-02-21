package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.concurrent.CompletableFutures;
import com.github.imthenico.annihilation.api.map.ConfigurableModelManager;
import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.storage.AnniStorage;
import com.github.imthenico.annihilation.api.storage.StorageHandlerProvider;
import com.github.imthenico.annihilation.api.storage.StorageHandlerProviderFactory;
import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;
import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;
import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.logging.Level;

public class SimplePluginHandler implements PluginHandler {

    private final FastAnnihilationPlugin plugin;
    private StorageHandlerProvider storageHandlerProvider;
    private AnniStorage storageHandler;
    private AnniConfig anniConfig;

    public SimplePluginHandler(FastAnnihilationPlugin plugin) {
        this.plugin = plugin;
        this.storageHandler = null;

        initConfig();
    }

    @Override
    public File getFolder() {
        File folder = plugin.getDataFolder();

        if (!folder.exists())
            Validate.isTrue(folder.mkdirs(), "Unable to create plugin folder");

        return folder;
    }

    @Override
    public Configuration getYaml() {
        return plugin.getConfiguration();
    }

    @Override
    public AnniConfig getPluginConfig() {
        return anniConfig;
    }

    @Override
    public Supplier<AnniStorage> getStorageHandler() {
        return () -> storageHandler;
    }

    @Override
    public void reloadConfig() {
        getYaml().loadFileContent(true);

        initConfig();
    }

    @Override
    public void reloadStorage() {
        reloadConfig();

        this.storageHandlerProvider = StorageHandlerProviderFactory.of(anniConfig.getStorageSourceTypeName());

        try {
            handleException(CompletableFutures.runAsync(() -> {
                try {
                    this.storageHandler = storageHandlerProvider.createHandler(plugin, anniConfig);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Duration.ofSeconds(20)), "Unable to reload storage data, disabling...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadMaps() {
        ConfigurableModelManager configurableModelManager = plugin.getAPI().getMapManager();

        try {
            handleException(configurableModelManager.reloadMaps(20), "Unable to reload maps, disabling...");
        } catch (Exception e) {
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadMessages() {
        MessageHandler messageHandler = plugin.getAPI().utilities().getMessageHandler();
        MessageSource messageSource = messageHandler.getSource();

        ((AbstractCachedFileSource<YamlConfiguration>) messageSource).invalidateCaches();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends JavaPlugin> T getPlugin() {
        return (T) plugin;
    }

    @Override
    public void reloadAll() {
        reloadStorage();
        reloadMessages();
    }

    private void initConfig() {
        this.anniConfig = getYaml().getObject("anni-config", AnniConfig.class);

        if (anniConfig == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to load annihilation config, disabling...");

            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private void handleException(CompletableFuture<?> future, String msg) {
        future.exceptionally(throwable -> {
            Bukkit.getLogger().log(Level.SEVERE, msg, throwable);

            Bukkit.getPluginManager().disablePlugin(plugin);

            return null;
        });
    }
}
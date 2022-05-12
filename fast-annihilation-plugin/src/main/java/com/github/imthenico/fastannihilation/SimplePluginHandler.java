package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.config.Configuration;
import me.yushust.message.MessageHandler;
import me.yushust.message.source.AbstractCachedFileSource;
import me.yushust.message.source.MessageSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class SimplePluginHandler implements PluginHandler {

    private final FastAnnihilationPlugin plugin;
    private AnniConfig anniConfig;

    public SimplePluginHandler(FastAnnihilationPlugin plugin) {
        this.plugin = plugin;

        initConfig();
    }

    @Override
    public File getFolder() {
        File folder = plugin.getDataFolder();

        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Unable to create plugin folder");
        }

        return folder;
    }

    @Override
    public Configuration getYaml() {
        return (Configuration) plugin.getConfig();
    }

    @Override
    public AnniConfig getPluginConfig() {
        return anniConfig;
    }

    @Override
    public void reloadConfig() {
        getYaml().loadFileContent(true);

        initConfig();
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
        reloadMessages();
    }

    private void initConfig() {
        this.anniConfig = (AnniConfig) getYaml().get("anni-config");

        if (anniConfig == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Unable to load annihilation config, disabling...");

            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }
}
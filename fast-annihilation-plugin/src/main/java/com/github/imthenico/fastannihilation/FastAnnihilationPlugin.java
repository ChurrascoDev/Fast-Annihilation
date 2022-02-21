package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.cache.SimpleMapModelCache;
import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.lang.AnniPlayerAdapter;
import com.github.imthenico.annihilation.api.lang.MatchPlayerAdapter;
import com.github.imthenico.annihilation.api.lang.PlayerLinguist;
import com.github.imthenico.annihilation.api.loader.MapModelStorage;
import com.github.imthenico.annihilation.api.loader.SimpleMapStorage;
import com.github.imthenico.annihilation.api.map.ConfigurableModelManager;
import com.github.imthenico.annihilation.api.map.SimpleConfigurableModelManager;
import com.github.imthenico.annihilation.api.property.SimpleDataInterpretation;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.provider.WorldTemplateLoader;
import com.github.imthenico.annihilation.api.scheduler.SimpleBukkitScheduler;
import com.github.imthenico.annihilation.api.task.GameTimerUpdater;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.storage.AnniStorage;
import com.github.imthenico.fastannihilation.template.SlimeWorldTemplateLoader;
import com.github.imthenico.simplecommons.bukkit.configuration.Configuration;
import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import me.yushust.message.MessageHandler;
import me.yushust.message.bukkit.YamlMessageSource;
import me.yushust.message.source.MessageSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.github.imthenico.fastannihilation.delegate.DelegatingFactory.*;

public final class FastAnnihilationPlugin extends JavaPlugin {

    private FastAnnihilation fastAnnihilation;
    private Configuration configuration;
    private PluginHandler pluginHandler;

    static {
        ConfigurationSerialization.registerClass(AnniConfig.class, "AnnihilationConfig");
    }

    @Override
    public void onEnable() {
        this.configuration = new Configuration(this, "config");

        this.pluginHandler = new SimplePluginHandler(this);

        MessageSource messageSource = new YamlMessageSource(
                this,
                new File(pluginHandler.getFolder(), "lang"),
                "messages_%lang%.yml"
        );

        PlayerRegistry playerRegistry = new PlayerRegistry();

        MessageHandler messageHandler = MessageHandler
                .of(messageSource, configurationHandle -> configurationHandle.specify(Player.class)
                        .resolveFrom(AnniPlayer.class, new AnniPlayerAdapter())
                        .resolveFrom(MatchPlayer.class, new MatchPlayerAdapter())
                        .setLinguist(new PlayerLinguist(playerRegistry)));

        SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

        SlimeLoader slimeLoader = slimePlugin.getLoader(pluginHandler.getPluginConfig().getSwmLoaderName());
        WorldTemplateLoader worldTemplateLoader = new SlimeWorldTemplateLoader(slimeLoader);

        AnniStorage anniStorage = newStorageHandler(pluginHandler.getStorageHandler());

        MapModelStorage mapModelStorage = new SimpleMapStorage(
                anniStorage.getModelDataRepository(),
                new SimpleDataInterpretation(),
                worldTemplateLoader,
                new SimpleBukkitScheduler(this)
        );

        ConfigurableModelManager configurableModelManager = new SimpleConfigurableModelManager(
                new SimpleMapModelCache(),
                mapModelStorage
        );

        this.fastAnnihilation = new FastAnnihilation(
                new UtilityPack(messageHandler),
                playerRegistry,
                configurableModelManager,
                new SimpleBukkitScheduler(this)
        );

        new GameTimerUpdater(fastAnnihilation.gameRegistry(), messageHandler)
                .runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        
    }

    @NotNull
    @Override
    public FileConfiguration getConfig() {
        return getConfiguration();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public FastAnnihilation getAPI() {
        return fastAnnihilation;
    }

    public static FastAnnihilationPlugin getInstance() {
        return JavaPlugin.getPlugin(FastAnnihilationPlugin.class);
    }
}
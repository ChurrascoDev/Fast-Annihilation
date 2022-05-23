package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.PluginHandler;
import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.config.Configuration;
import com.github.imthenico.annihilation.api.db.UserCredential;
import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.fastannihilation.lang.AnniPlayerAdapter;
import com.github.imthenico.fastannihilation.lang.MatchPlayerAdapter;
import com.github.imthenico.fastannihilation.lang.PlayerLinguist;
import com.github.imthenico.fastannihilation.lang.SimpleAnniMessageSender;
import com.github.imthenico.fastannihilation.listener.def.DefaultsListenerModule;
import com.github.imthenico.fastannihilation.listener.def.game.GameListenerModule;
import com.github.imthenico.fastannihilation.listener.def.match.MatchListenerModule;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.registry.ModelType;
import com.github.imthenico.annihilation.api.registry.ModelTypeRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.fastannihilation.papi.NexusExpansion;
import com.github.imthenico.fastannihilation.scheduler.SimpleBukkitScheduler;
import com.github.imthenico.annihilation.api.service.ModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import com.github.imthenico.fastannihilation.papi.AnniPlayerExpansion;
import com.github.imthenico.fastannihilation.papi.GameExpansion;
import com.github.imthenico.fastannihilation.papi.TeamExpansion;
import com.github.imthenico.fastannihilation.service.GameServiceImpl;
import com.github.imthenico.fastannihilation.service.ModelServiceImpl;
import com.github.imthenico.fastannihilation.service.PluginStorageService;
import com.github.imthenico.fastannihilation.service.ScoreboardServiceImpl;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.fastannihilation.task.GameTimerUpdater;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.fastannihilation.command.*;
import com.github.imthenico.gmlib.pool.TemplatePool;
import com.github.imthenico.gmlib.swm.SWMWorldLoader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grinderwolf.swm.plugin.SWMPlugin;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.command.Command;
import me.yushust.message.MessageHandler;
import me.yushust.message.bukkit.YamlMessageSource;
import me.yushust.message.source.MessageSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FastAnnihilationPlugin extends JavaPlugin {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private FastAnnihilationAPI fastAnnihilation;
    private CommandManager commandManager;
    private PartInjector partInjector;
    private Configuration configuration;
    private PluginHandler pluginHandler;
    private PlayerRegistry playerRegistry;
    private UtilityPack utilityPack;
    private Scheduler scheduler;
    private TemplatePool templatePool;

    private ModelService modelService;
    private GameService gameService;
    private PluginStorageService storageService;
    private ScoreboardService scoreboardService;

    static {
        ConfigurationSerialization.registerClass(AnniConfig.class, "AnnihilationConfig");
        ConfigurationSerialization.registerClass(UserCredential.class, "AnniStorageCredential");
    }

    @Override
    public void onEnable() {
        this.configuration = new Configuration(this, "config.yml");

        // storage load
        pluginHandler = new SimplePluginHandler(this);

        // player registry
        playerRegistry = new PlayerRegistry();

        // utilities
        scheduler = new SimpleBukkitScheduler(this);
        createUtilityPack();

        // services
        loadServices();

        // start timer for games
        new GameTimerUpdater(gameService.gameManager(), utilityPack.getMessageHandler())
                .runTaskTimer(this, 0, 20);

        // register API in Bukkit services manager
        registerAPIService();

        // default model types
        registerDefModelTypes();

        // commands
        commandManager = new BukkitCommandManager("annihilation");
        createPartInjector();
        registerCommands();

        // listeners
        registerListeners();

        // placeholders
        registerPAPIExpansions();
    }

    @Override
    public void onDisable() {
        modelService.end();
        gameService.end();
        storageService.end();
        scoreboardService.end();
    }

    @Override
    public FileConfiguration getConfig() {
        return configuration;
    }

    public FastAnnihilationAPI getAPI() {
        return fastAnnihilation;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public GameService getGameService() {
        return gameService;
    }

    public ScoreboardService getScoreboardService() {
        return scoreboardService;
    }

    private void registerAPIService() {
        this.fastAnnihilation = new FastAnnihilationAPI(
                utilityPack,
                playerRegistry,
                this,
                scheduler,
                pluginHandler
        );

        ServicesManager servicesManager = Bukkit.getServicesManager();

        servicesManager.register(AnnihilationAPI.class, fastAnnihilation, this, ServicePriority.Low);
    }

    private void registerDefModelTypes() {
        ModelTypeRegistry registry = fastAnnihilation.getModelTypeRegistry();

        registry.register("anni-match", ModelType.of(MatchMapData.class, "The default annihilation match model type", MatchMapData::new));
        registry.register("anni-lobby", ModelType.of(GameLobbyData.class, "The default annihilation lobby model type", GameLobbyData::new));
    }

    private void createPartInjector() {
        partInjector = PartInjector.create();

        partInjector.install(new DefaultsModule());
        partInjector.install(new AnniCommandModule(playerRegistry));
    }

    private void registerCommands() {
        AnnotatedCommandTreeBuilder commandTreeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);

        List<Command> commandsToRegister = new ArrayList<>();

        addToList(
                commandsToRegister,
                commandTreeBuilder,
                new GameInstanceManagerCommand(
                        gameService.gameManager(),
                        gameService.factory(),
                        modelService.getModelStorage().getCachedModels()
                )
        );

        addToList(
                commandsToRegister,
                commandTreeBuilder,
                new ConfigurableModelManagerCommand(
                        fastAnnihilation.getModelTypeRegistry(),
                        modelService.getModelStorage(),
                        templatePool,
                        modelService.getModelSetupManager()
                )
        );

        addToList(
                commandsToRegister,
                commandTreeBuilder,
                new MapSetupCommand(modelService.getModelSetupManager())
        );

        addToList(commandsToRegister, commandTreeBuilder, new SelectTeamCommand());
        addToList(commandsToRegister, commandTreeBuilder, new GameCommand(utilityPack.getMessageHandler()));

        commandManager.registerCommands(commandsToRegister);
    }

    private void addToList(List<Command> commands, AnnotatedCommandTreeBuilder builder, CommandClass commandClass) {
        commands.addAll(builder.fromClass(commandClass));
    }

    private void createUtilityPack() {
        MessageSource messageSource = new YamlMessageSource(
                this,
                new File(pluginHandler.getFolder(), "lang"),
                "messages_%lang%.yml"
        );

        MessageHandler messageHandler = MessageHandler.of(messageSource, configurationHandle ->
                configurationHandle.specify(Player.class)
                        .resolveFrom(AnniPlayer.class, new AnniPlayerAdapter())
                        .resolveFrom(MatchPlayer.class, new MatchPlayerAdapter())
                        .setLinguist(new PlayerLinguist(playerRegistry))
                        .setMessageSender(new SimpleAnniMessageSender())
        );

        utilityPack = new UtilityPack(messageHandler);
    }

    private void loadServices() {
        storageService = new PluginStorageService(
                pluginHandler,
                gson,
                this
        );

        storageService.start();

        AnniStorage anniStorage = storageService.getAnniStorage();

        templatePool = TemplatePool.create(new SWMWorldLoader());

        modelService = new ModelServiceImpl(
                anniStorage.getModelDataRepository(),
                gson,
                templatePool
        );

        modelService.start();

        Location location = (Location) configuration.get("lobby-spawn");

        if (location == null)
            location = Bukkit.getWorlds().get(0).getSpawnLocation();

        gameService = new GameServiceImpl(
                utilityPack,
                location,
                modelService.getModelStorage().getCachedModels(),
                modelService.getGameMapHandler()
        );

        gameService.start();

        scoreboardService = new ScoreboardServiceImpl();

        scoreboardService.start();
    }

    private void registerListeners() {
        Consumer<Listener> consumer = listener -> Bukkit.getPluginManager().registerEvents(listener, FastAnnihilationPlugin.this);

        new DefaultsListenerModule(modelService.getModelSetupManager(), playerRegistry, gameService.getLobbySpawnReference(), scoreboardService)
                .getListeners().forEach(consumer);

        new MatchListenerModule("anni-match", utilityPack.getMessageHandler(), scheduler, playerRegistry)
                .getListeners().forEach(consumer);

        new GameListenerModule()
                .getListeners().forEach(consumer);
    }

    private void registerPAPIExpansions() {
        new GameExpansion(playerRegistry).register();
        new AnniPlayerExpansion(playerRegistry).register();
        new TeamExpansion().register();
        new NexusExpansion(playerRegistry).register();
    }

    public static FastAnnihilationPlugin getInstance() {
        return JavaPlugin.getPlugin(FastAnnihilationPlugin.class);
    }

}
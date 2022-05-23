package com.github.imthenico.fastannihilation.service;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.fastannihilation.editor.SimpleAbstractSetupManager;
import com.github.imthenico.fastannihilation.model.AnniModule;
import com.github.imthenico.annihilation.api.model.MapModelStorage;
import com.github.imthenico.fastannihilation.model.MapModelStorageImpl;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.service.ModelService;
import com.github.imthenico.annihilation.api.util.CompletableFutures;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.handler.HandlerRegistry;
import com.github.imthenico.gmlib.pool.TemplatePool;
import com.github.imthenico.repository.AbstractRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import static org.bukkit.ChatColor.*;

public class ModelServiceImpl implements ModelService {

    private final MapModelStorage modelStorageService;
    private final GameMapHandler gameMapHandler;
    private final ModelSetupManager modelSetupManager;
    private final TemplatePool templatePool;

    public ModelServiceImpl(
            AbstractRepository<JsonObject> modelDataRepository,
            Gson gson,
            TemplatePool templatePool
    ) {
        HandlerRegistry dataManipulation = HandlerRegistry.builder()
                .consume(new AnniModule(gson))
                .build();

        gameMapHandler = GameMapHandler.create(
                templatePool,
                dataManipulation,
                gson
        );

        this.modelStorageService = new MapModelStorageImpl(modelDataRepository, gameMapHandler);
        this.modelSetupManager = new SimpleAbstractSetupManager();
        this.templatePool = templatePool;
    }

    @Override
    public CompletableFuture<?> reloadModels(int timeOut) {
        ConsoleCommandSender sender = Bukkit.getConsoleSender();

        sender.sendMessage(YELLOW + "Loading models");

        long current = System.currentTimeMillis();

        return CompletableFutures.timed(
                modelStorageService.getAllFromDataAsync(),
                Duration.ofSeconds(timeOut),
                "Unable to load models after 20 seconds"
        ).whenComplete((models, exc) -> {
            ModelCache modelCache = modelStorageService.getCachedModels();

            models.forEach(modelCache::addModel);

            long time = System.currentTimeMillis() - current;

            if (models.isEmpty()) {
                sender.sendMessage(BLUE + "No models loaded");
                return;
            }

            sender.sendMessage(
                    modelCache.count() + (GREEN + String.format(" models loaded in %sms", time))
            );
        });
    }

    @Override
    public MapModelStorage getModelStorage() {
        return modelStorageService;
    }

    @Override
    public GameMapHandler getGameMapHandler() {
        return gameMapHandler;
    }

    @Override
    public ModelSetupManager getModelSetupManager() {
        return modelSetupManager;
    }

    @Override
    public TemplatePool getTemplatePool() {
        return templatePool;
    }

    @Override
    public void start() {
        reloadModels(1);
    }

    @Override
    public void end() {
        modelStorageService
                .getCachedModels()
                .forEach(modelStorageService::save);
        Bukkit.getLogger().log(Level.INFO, "Saved Models");
    }
}
package com.github.imthenico.annihilation.api.service;

import com.github.imthenico.annihilation.api.cache.ConfigurableModelCache;
import com.github.imthenico.annihilation.api.cache.SimpleConfigurableModelCache;
import com.github.imthenico.annihilation.api.concurrent.CompletableFutures;
import com.github.imthenico.annihilation.api.editor.SetupManager;
import com.github.imthenico.annihilation.api.editor.WithSavingSetupManager;
import com.github.imthenico.annihilation.api.property.PropertyMapping;
import com.github.imthenico.annihilation.api.provider.WorldTemplateLoader;
import com.github.imthenico.annihilation.api.repository.ConfigurableModelRepository;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.storage.AnniStorage;
import com.github.imthenico.simplecommons.bukkit.service.AbstractPluginService;
import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class ConfigurableModelService extends AbstractPluginService {

    private final SetupManager setupManager;
    private final ConfigurableModelCache configurableModelCache;
    private final ConfigurableModelRepository configurableModelRepository;

    public ConfigurableModelService(
            AnniStorage anniStorage,
            PropertyMapping propertyMapping,
            WorldTemplateLoader worldTemplateLoader,
            Scheduler scheduler
    ) {
        this.configurableModelCache = new SimpleConfigurableModelCache();
        this.configurableModelRepository = new ConfigurableModelRepository(
                anniStorage.getModelDataRepository(),
                propertyMapping,
                worldTemplateLoader,
                scheduler
        );

        this.setupManager = new WithSavingSetupManager(configurableModelRepository);
    }

    @Override
    protected void onEnd() {
        Bukkit.getLogger().log(Level.INFO, "Saving models");

        cache()
                .getModels()
                .forEach((k, v) -> configurableModelRepository.save(v, new SimpleSourceKey(k)));
    }

    @Override
    protected void onStart() {
        reloadModels(30);
    }

    public CompletableFuture<?> reloadModels(int timeOut) {
        Bukkit.getLogger().log(Level.INFO, "Loading models");

        long current = System.currentTimeMillis();

        return CompletableFutures.timed(
                configurableModelRepository.asyncAllCollection(),
                Duration.ofSeconds(timeOut)
        ).whenComplete((models, exc) -> {
            models.forEach(cache()::addModel);

            long time = System.currentTimeMillis() - current;

            Bukkit.getLogger().log(Level.INFO,
                    models.size() + String.format(" models loaded in %sms", time));
        });
    }

    public ConfigurableModelCache cache() {
        return configurableModelCache;
    }

    public ConfigurableModelRepository repository() {
        return configurableModelRepository;
    }

    public SetupManager setupManager() {
        return setupManager;
    }
}
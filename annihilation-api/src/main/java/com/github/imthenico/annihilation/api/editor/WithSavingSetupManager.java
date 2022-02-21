package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.concurrent.CompletableFutures;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.simplecommons.data.key.SimpleSourceKey;
import com.github.imthenico.simplecommons.data.repository.service.SavingService;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class WithSavingSetupManager extends AbstractSetupManager {

    private final SavingService<ConfigurableModel> savingService;

    public WithSavingSetupManager(SavingService savingService) {
        this.savingService = savingService;
    }

    @Override
    public CompletableFuture<?> saveChanges(ConfigurableModel model) throws IllegalArgumentException {
        long current = System.currentTimeMillis();

        return CompletableFutures.timed(
                this.savingService.asyncSave(model, new SimpleSourceKey(model.getId())),
                Duration.ofSeconds(20)
        ).whenComplete((a, exc) -> logSaving(model, current));
    }

    private void logSaving(ConfigurableModel model, long millis) {
        millis = System.currentTimeMillis() - millis;
        Bukkit.getLogger().log(Level.INFO, String.format("%s saved in %sms", model.getId(), millis));
    }
}
package com.github.imthenico.annihilation.api.service;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.annihilation.api.model.MapModelStorage;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.pool.WorldPool;

import java.util.concurrent.CompletableFuture;

public interface ModelService extends Service {

    CompletableFuture<?> reloadModels(int timeOut);

    MapModelStorage getModelStorage();

    GameMapHandler getGameMapHandler();

    ModelSetupManager getModelSetupManager();

    WorldPool getWorldPool();
}
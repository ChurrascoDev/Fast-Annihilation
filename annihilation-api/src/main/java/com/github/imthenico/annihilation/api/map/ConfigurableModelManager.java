package com.github.imthenico.annihilation.api.map;

import com.github.imthenico.annihilation.api.cache.MapModelCache;
import com.github.imthenico.annihilation.api.loader.MapModelStorage;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.concurrent.CompletableFuture;

public interface ConfigurableModelManager extends MapModelCache, MapModelStorage {

    CompletableFuture<?> reloadMaps(int timeOut) throws Exception;

    static ConfigurableModelManager create(
            MapModelCache modelCache,
            MapModelStorage modelRepository
    ) {
        return new SimpleConfigurableModelManager(
                Validate.notNull(modelCache),
                Validate.notNull(modelRepository)
        );
    }
}
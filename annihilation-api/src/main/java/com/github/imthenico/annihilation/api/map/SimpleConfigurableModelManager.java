package com.github.imthenico.annihilation.api.map;

import com.github.imthenico.annihilation.api.cache.MapModelCache;
import com.github.imthenico.annihilation.api.concurrent.CompletableFutures;
import com.github.imthenico.annihilation.api.exception.NoPropertiesFoundException;
import com.github.imthenico.annihilation.api.exception.UnknownWorldException;
import com.github.imthenico.annihilation.api.loader.MapModelStorage;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.simplecommons.data.key.SourceKey;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SimpleConfigurableModelManager implements ConfigurableModelManager {

    private final MapModelCache mapModelCache;
    private final MapModelStorage modelRepository;

    public SimpleConfigurableModelManager(
            MapModelCache mapModelCache,
            MapModelStorage modelRepository
    ) {
        this.mapModelCache = mapModelCache;
        this.modelRepository = modelRepository;
    }

    @Override
    public CompletableFuture<?> reloadMaps(int timeOut) {
        mapModelCache.clear();

        return CompletableFutures
                .timed(modelRepository.asyncAllCollection(), Duration.ofSeconds(timeOut))
                .whenComplete((models, exc) -> models.forEach(mapModelCache::addModel));
    }

    @Override
    public void addModel(ConfigurableModel model) {
        mapModelCache.addModel(model);
    }

    @Override
    public ConfigurableModel removeModel(String name) {
        return mapModelCache.removeModel(name);
    }

    @Override
    public ConfigurableModel getModel(String name) {
        return mapModelCache.getModel(name);
    }

    @Override
    public Map<String, ConfigurableModel> getModels() {
        return mapModelCache.getModels();
    }

    @Override
    public void clear() {
        mapModelCache.clear();
    }

    @Override
    public boolean has(String name) {
        return mapModelCache.has(name);
    }

    @Override
    public CompletableFuture<ConfigurableModel> asyncFind(SourceKey sourceKey) {
        return modelRepository.asyncFind(sourceKey);
    }

    @Override
    public CompletableFuture<Set<ConfigurableModel>> asyncAllCollection() {
        return modelRepository.asyncAllCollection();
    }

    @Override
    public CompletableFuture<Set<SourceKey>> asyncKeyCollection() {
        return modelRepository.asyncKeyCollection();
    }

    @Override
    public ConfigurableModel usingId(SourceKey sourceKey) throws UnsupportedOperationException, NoPropertiesFoundException, UnknownWorldException {
        return modelRepository.usingId(sourceKey);
    }

    @Override
    public Set<ConfigurableModel> all() {
        return modelRepository.all();
    }

    @Override
    public Set<SourceKey> keys() {
        return modelRepository.keys();
    }

    @Override
    public CompletableFuture<Integer> asyncDelete(SourceKey sourceKey) {
        return modelRepository.asyncDelete(sourceKey);
    }

    @Override
    public int delete(SourceKey sourceKey) {
        return modelRepository.delete(sourceKey);
    }

    @Override
    public CompletableFuture<?> asyncSave(ConfigurableModel model, SourceKey sourceKey) {
        return modelRepository.asyncSave(model, sourceKey);
    }

    @Override
    public void save(ConfigurableModel model, SourceKey sourceKey) {
        modelRepository.save(model, sourceKey);
    }
}
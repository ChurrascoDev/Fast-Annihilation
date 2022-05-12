package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.util.SafeCast;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleModelCache implements ModelCache {

    private final Map<String, MapModel<?>> models;

    public SimpleModelCache() {
        this.models = new ConcurrentHashMap<>();
    }

    @Override
    public boolean addModel(MapModel<?> model) {
        if (models.containsKey(model.getName()))
            return false;

        models.put(model.getName(), model);
        return true;
    }

    @Override
    public <T extends ModelData> MapModel<?> replaceModel(MapModel<T> mapModel) {
        return models.remove(mapModel.getName());
    }

    @Override
    public void removeModel(String name) {
        models.remove(name);
    }

    @Override
    public <T extends ModelData> MapModel<T> getModel(String name) {
        return SafeCast.of(models.get(name)).orDefault(() -> null);
    }

    @Override
    public void clear() {
        models.clear();
    }

    @Override
    public boolean has(String name) {
        return models.containsKey(name);
    }

    @Override
    public int count() {
        return models.size();
    }

    @NotNull
    @Override
    public Iterator<MapModel<?>> iterator() {
        Iterator<MapModel<?>> iterator = models.values().iterator();

        return new Iterator<MapModel<?>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public MapModel<?> next() {
                return iterator.next();
            }
        };
    }
}
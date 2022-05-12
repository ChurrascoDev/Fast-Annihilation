package com.github.imthenico.fastannihilation.strategy;

import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.gmlib.MapModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DefaultMatchMapModelProvider implements MatchMapModelProvider {

    private final ModelCache modelCache;

    public DefaultMatchMapModelProvider(ModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public @NotNull MapModel<MatchMapData> getModel(String name) {
        return modelCache.getModel(name);
    }

    @Override
    public @NotNull List<String> getAvailableModelNames() {
        List<String> modelNames = new ArrayList<>();

        for (MapModel<?> mapModel : modelCache) {
            modelNames.add(mapModel.getName());
        }

        return modelNames;
    }
}
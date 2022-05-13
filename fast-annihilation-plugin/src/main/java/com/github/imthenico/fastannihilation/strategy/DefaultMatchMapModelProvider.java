package com.github.imthenico.fastannihilation.strategy;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;
import com.github.imthenico.gmlib.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultMatchMapModelProvider implements MatchMapModelProvider {

    private final static MapCandidateValidator DELEGATE = MapCandidateValidator.defaultValidator();

    private final ModelCache modelCache;

    public DefaultMatchMapModelProvider(ModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public @Nullable MapModel<MatchMapData> getModel(String name) {
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

    @Override
    public boolean isValid(String mapName, Game game) {
        return DELEGATE.isValid(mapName, game);
    }
}
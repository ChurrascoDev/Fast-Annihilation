package com.github.imthenico.annihilation.api.strategy;

import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;
import com.github.imthenico.gmlib.MapModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface MatchMapModelProvider extends MapCandidateValidator {

    @Nullable MapModel<MatchMapData> getModel(String name);

    @NotNull List<String> getAvailableModelNames();
    
}
package com.github.imthenico.annihilation.api.validator;

import com.github.imthenico.annihilation.api.game.Game;

public interface MapCandidateValidator  {

    boolean isValid(String mapName, Game game);

    static MapCandidateValidator defaultValidator() {
        return (mapName, game) -> game.getRules().getAllowedMaps().contains(mapName);
    }
}
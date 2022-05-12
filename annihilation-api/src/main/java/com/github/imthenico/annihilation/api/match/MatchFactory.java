package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.MapModel;

public interface MatchFactory {

    Match createMatch(
            Game game,
            MatchExpansion gameExpansion,
            MapModel<? extends MatchMapData> mapModel
    );
}
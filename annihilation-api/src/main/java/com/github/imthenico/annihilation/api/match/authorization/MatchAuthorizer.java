package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.MapModel;

public interface MatchAuthorizer {

    AuthorizationResult canStart(Game game);

    AuthorizationResult isEligibleForMap(MapModel<? extends MatchMapData> model);

}
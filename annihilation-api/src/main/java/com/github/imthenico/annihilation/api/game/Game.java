package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.MapModel;

public interface Game {
    GameState calculateState();

    AuthorizationResult startMatch();

    AuthorizationResult startMatch(MapModel<? extends MatchMapData> suggestedModel);

    Match runningMatch();

    MatchAuthorizer getMatchAuthorizer();

    PreMatchStage getPreparationStage();

    String getTypeName();

    Options getOptions();

    Rules getRules();

    GameRoom room();

}
package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;

public interface Game {
    GameState calculateState();

    AuthorizationResult startMatch();

    Match runningMatch();

    MatchAuthorizer getMatchAuthorizer();

    PreMatchStage getPreparationStage();

    String getTypeName();

    Options getOptions();

    Rules getRules();

    GameRoom room();

}
package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.gmlib.MapModel;

import java.util.List;
import java.util.function.Predicate;

public interface Game {
    GameState calculateState();

    AuthorizationResult startMatch();

    AuthorizationResult startMatch(MapModel<? extends MatchMapData> suggestedModel);

    Match runningMatch();

    MatchAuthorizer getMatchAuthorizer();

    MatchMapModelProvider getMatchMapModelProvider();

    PreMatchStage getPreparationStage();

    String getTypeName();

    Options getOptions();

    Rules getRules();

    GameRoom room();

    List<AnniPlayer> getPlayers(Predicate<AnniPlayer> filter);

    List<AnniPlayer> getPlayers();
}
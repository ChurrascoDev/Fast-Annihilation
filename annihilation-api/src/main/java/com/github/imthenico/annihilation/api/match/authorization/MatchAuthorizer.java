package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.game.Game;

public interface MatchAuthorizer {

    AuthorizationResult canStart(Game game);

}
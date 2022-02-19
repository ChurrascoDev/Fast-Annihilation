package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.game.GameInstance;

public interface MatchAuthorizer {

    AuthorizationResult canStart(GameInstance game);

}
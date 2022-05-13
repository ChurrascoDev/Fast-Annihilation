package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;

public interface GameExpansion {

    MatchExpansion getMatchExpansion();

    Options newOptions();

    Rules newRules();

    MatchAuthorizer newAuthorizer();

    MatchMapModelProvider newMatchMapModelProvider();

}
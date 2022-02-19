package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.match.Match;

@FunctionalInterface
public interface PlayerSetup {

    void setupPlayer(MatchPlayer matchPlayer, Match match);

}
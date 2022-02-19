package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.ingame.Nexus;

public interface MatchEventHandler {

    void handleNexusDamage(MatchPlayer matchPlayer, int damage, Nexus nexus);

    void handleNexusDeath(Nexus nexus);

    void handleMatchStart(Match match);

    void handleMatchEnd(MatchState matchState);

}
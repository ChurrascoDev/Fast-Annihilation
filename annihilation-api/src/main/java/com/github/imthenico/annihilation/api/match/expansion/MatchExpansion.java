package com.github.imthenico.annihilation.api.match.expansion;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.player.PlayerSetup;

import java.util.function.Function;

public interface MatchExpansion {

    PhaseExpansion getPhaseExpansion();

    Function<Game, MatchClosingStage> getEndingProvider();

    PlayerSetup getPlayerSetup();

}
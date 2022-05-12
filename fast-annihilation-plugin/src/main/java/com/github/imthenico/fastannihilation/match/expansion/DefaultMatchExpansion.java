package com.github.imthenico.fastannihilation.match.expansion;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.fastannihilation.match.DefaultMatchClosingStage;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.fastannihilation.phase.DefaultPhaseExpansion;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.fastannihilation.player.SimplePlayerSetup;
import me.yushust.message.MessageHandler;

import java.util.function.Function;

public class DefaultMatchExpansion implements MatchExpansion {

    private final PhaseExpansion phaseExpansion;
    private final Function<Game, MatchClosingStage> endingProvider;
    private final PlayerSetup playerSetup;

    public DefaultMatchExpansion(MessageHandler messageHandler) {
        this.phaseExpansion = new DefaultPhaseExpansion(messageHandler);
        this.endingProvider = game -> new DefaultMatchClosingStage(game.getRules().getTimeToEnd());
        this.playerSetup = new SimplePlayerSetup();
    }

    @Override
    public PhaseExpansion getPhaseExpansion() {
        return phaseExpansion;
    }

    @Override
    public Function<Game, MatchClosingStage> getEndingProvider() {
        return endingProvider;
    }

    @Override
    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

}
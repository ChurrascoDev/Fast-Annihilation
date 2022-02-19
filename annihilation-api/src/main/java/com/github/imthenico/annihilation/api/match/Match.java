package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerEventHandler;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.util.SimpleTimer;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public interface Match {

    MatchMap getRunningMap();

    MatchEventHandler getEventHandler();

    GameInstance getGame();

    PhaseManager getPhaseManager();

    PlayerSetup getPlayerSetup();

    PlayerEventHandler getPlayerEventHandler();

    void leave(AnniPlayer player);

    void join(AnniPlayer player);

    MatchPlayer getPlayer(AnniPlayer player);

    void setDisqualified(UUID uuid, boolean disqualified);

    boolean isDisqualified(UUID uuid);

    boolean finalized();

    void terminate() throws UnsupportedOperationException;

    MatchClosingStage getClosingStage();

    boolean allPhasesFinished();

    void start() throws UnsupportedOperationException;

    void end();

    default MatchState getState() {
        return MatchState.of(this);
    }

    SimpleTimer getActiveTimer();

    Collection<MatchPlayer> getPlayers(
            Predicate<MatchPlayer> filter
    );

    default Collection<MatchPlayer> getPlayers() {
        return getPlayers((matchPlayer) -> true);
    }

    /**
     * This method is used to identify different types of
     * matches with different game functions, characteristics, etc.
     */
    String getTypeName();
}
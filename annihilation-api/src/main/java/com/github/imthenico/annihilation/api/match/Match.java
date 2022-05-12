package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.util.SimpleTimer;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

public interface Match {

    MatchMap getRunningMap();

    Game getGame();

    PhaseManager getPhaseManager();

    PlayerSetup getPlayerSetup();

    void handleLeave(MatchPlayer matchPlayer);

    MatchPlayer handleJoin(AnniPlayer player);

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
     * games with different game functions, characteristics, etc.
     */
    String getTypeName();
}
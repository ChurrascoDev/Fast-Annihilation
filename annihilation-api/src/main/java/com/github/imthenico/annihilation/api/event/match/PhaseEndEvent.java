package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.phase.Phase;
import java.util.Objects;
import org.bukkit.event.HandlerList;

public class PhaseEndEvent extends MatchEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Phase phase;

    public PhaseEndEvent(Match match, Phase phase) {
        super(match);
        this.phase = Objects.requireNonNull(phase);
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
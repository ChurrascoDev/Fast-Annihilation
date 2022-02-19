package com.github.imthenico.annihilation.api.event;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.phase.Phase;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PhaseStartEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Phase phase;
    private final Match match;

    public PhaseStartEvent(
            Phase phase,
            Match match
    ) {
        this.phase = Validate.notNull(phase);
        this.match = Validate.notNull(match);
    }

    public Phase getPhase() {
        return phase;
    }

    public Match getMatch() {
        return match;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
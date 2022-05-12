package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.match.Match;
import java.util.Objects;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class MatchCreationEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Match match;

    public MatchCreationEvent(Match match) {
        this.match = Objects.requireNonNull(match);
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
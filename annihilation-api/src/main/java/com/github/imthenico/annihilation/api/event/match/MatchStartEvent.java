package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.HandlerList;

public class MatchStartEvent extends MatchEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public MatchStartEvent(Match match) {
        super(match);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
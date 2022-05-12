package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.PlayerMatchEvent;
import org.bukkit.event.HandlerList;

public class PlayerLeaveMatchEvent extends PlayerMatchEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerLeaveMatchEvent(MatchPlayer matchPlayer) {
        super(matchPlayer);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
package com.github.imthenico.annihilation.api.event;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMatchEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final MatchPlayer matchPlayer;
    private final Match match;

    public PlayerMatchEvent(MatchPlayer matchPlayer) {
        this.matchPlayer = matchPlayer;
        this.match = matchPlayer.getMatch();
    }

    public Match getMatch() {
        return match;
    }

    public MatchPlayer getMatchPlayer() {
        return matchPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
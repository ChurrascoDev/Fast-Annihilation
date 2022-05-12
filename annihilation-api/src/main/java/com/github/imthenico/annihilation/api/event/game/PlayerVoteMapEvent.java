package com.github.imthenico.annihilation.api.event.game;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import org.bukkit.event.HandlerList;

public class PlayerVoteMapEvent extends GameEvent {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final String vote;
    private final String lastVote;
    private final AnniPlayer voter;

    public PlayerVoteMapEvent(Game game, String vote, String lastVote, AnniPlayer voter) {
        super(game);
        this.vote = vote;
        this.lastVote = lastVote;
        this.voter = voter;
    }

    public String getVote() {
        return vote;
    }

    public String getLastVote() {
        return lastVote;
    }

    public AnniPlayer getVoter() {
        return voter;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
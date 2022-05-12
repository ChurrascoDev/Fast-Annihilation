package com.github.imthenico.annihilation.api.event.game;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerSelectTeamEvent extends GameEvent implements Cancellable {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final AnniPlayer player;
    private final TeamColor color;
    private final TeamColor lastTeam;
    private boolean cancelled;

    public PlayerSelectTeamEvent(Game game, AnniPlayer player, TeamColor color, TeamColor lastTeam) {
        super(game);
        this.player = player;
        this.color = color;
        this.lastTeam = lastTeam;
    }

    public AnniPlayer getPlayer() {
        return player;
    }

    public TeamColor getTeam() {
        return color;
    }

    public TeamColor getLastTeam() {
        return lastTeam;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
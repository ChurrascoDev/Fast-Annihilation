package com.github.imthenico.annihilation.api.event.game;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import org.bukkit.event.HandlerList;

public class PlayerUnselectTeamEvent extends GameEvent {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final AnniPlayer anniPlayer;
    private final TeamColor lastTeam;

    public PlayerUnselectTeamEvent(Game game, AnniPlayer anniPlayer, TeamColor lastTeam) {
        super(game);
        this.anniPlayer = anniPlayer;
        this.lastTeam = lastTeam;
    }

    public AnniPlayer getAnniPlayer() {
        return anniPlayer;
    }

    public TeamColor getLastTeam() {
        return lastTeam;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
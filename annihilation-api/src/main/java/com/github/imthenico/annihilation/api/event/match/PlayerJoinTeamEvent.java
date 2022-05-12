package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import org.bukkit.event.HandlerList;

public class PlayerJoinTeamEvent extends PlayerMatchEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final MatchTeam newTeam;
    private final MatchTeam oldTeam;

    public PlayerJoinTeamEvent(MatchPlayer matchPlayer, MatchTeam newTeam, MatchTeam oldTeam) {
        super(matchPlayer);
        this.newTeam = newTeam;
        this.oldTeam = oldTeam;
    }

    public MatchTeam getNewTeam() {
        return newTeam;
    }

    public MatchTeam getOldTeam() {
        return oldTeam;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
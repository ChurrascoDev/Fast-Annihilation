package com.github.imthenico.annihilation.api.event;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;

public class PlayerLeaveMatchEvent extends PlayerMatchEvent {
    public PlayerLeaveMatchEvent(MatchPlayer matchPlayer) {
        super(matchPlayer);
    }
}
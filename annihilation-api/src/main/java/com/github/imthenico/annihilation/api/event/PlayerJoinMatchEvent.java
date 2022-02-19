package com.github.imthenico.annihilation.api.event;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;

public class PlayerJoinMatchEvent extends PlayerMatchEvent {
    public PlayerJoinMatchEvent(MatchPlayer matchPlayer) {
        super(matchPlayer);
    }
}
package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;

public abstract class PlayerMatchEvent extends MatchEvent {

    private final MatchPlayer matchPlayer;

    public PlayerMatchEvent(MatchPlayer matchPlayer) {
        super(matchPlayer.getMatch());
        this.matchPlayer = matchPlayer;
    }

    public MatchPlayer getMatchPlayer() {
        return matchPlayer;
    }

}
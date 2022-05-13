package com.github.imthenico.fastannihilation.map;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.util.VoteCounter;

import java.util.List;

public class MapVoteCounter<K> extends VoteCounter<K, String> {

    private final MatchMapModelProvider matchMapModelProvider;
    private final Game game;

    public MapVoteCounter(MatchMapModelProvider matchMapModelProvider, Game game) {
        this.matchMapModelProvider = matchMapModelProvider;
        this.game = game;
    }

    @Override
    public List<String> getCandidates() {
        List<String> candidates = matchMapModelProvider.getAvailableModelNames();

        candidates.removeIf(candidate -> !matchMapModelProvider.isValid(candidate, game));

        return candidates;
    }
}
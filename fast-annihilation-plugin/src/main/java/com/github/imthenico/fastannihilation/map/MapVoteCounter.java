package com.github.imthenico.fastannihilation.map;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;

import java.util.List;

public class MapVoteCounter<K> extends VoteCounter<K, String> {

    private final MatchMapModelProvider matchMapModelProvider;
    private final MapCandidateValidator candidateValidator;
    private final Game game;

    public MapVoteCounter(MatchMapModelProvider matchMapModelProvider, MapCandidateValidator candidateValidator, Game game) {
        this.matchMapModelProvider = matchMapModelProvider;
        this.candidateValidator = candidateValidator;
        this.game = game;
    }

    @Override
    public List<String> getCandidates() {
        List<String> candidates = matchMapModelProvider.getAvailableModelNames();

        candidates.removeIf(candidate -> !candidateValidator.isValid(candidate, game));

        return candidates;
    }
}
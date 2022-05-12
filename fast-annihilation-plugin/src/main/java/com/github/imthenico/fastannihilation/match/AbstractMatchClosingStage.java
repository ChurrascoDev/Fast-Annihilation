package com.github.imthenico.fastannihilation.match;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;

import java.util.Objects;

public abstract class AbstractMatchClosingStage implements MatchClosingStage {

    private Match match;

    @Override
    public void setMatch(Match match) {
        this.match = Objects.requireNonNull(match);
    }

    protected Match match() {
        return match;
    }
}
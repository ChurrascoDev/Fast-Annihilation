package com.github.imthenico.annihilation.api.match;

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
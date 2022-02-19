package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.simplecommons.util.Validate;

public abstract class AbstractMatchClosingStage implements MatchClosingStage {

    private Match match;

    @Override
    public void setMatch(Match match) {
        this.match = Validate.notNull(match);
    }

    protected Match match() {
        return match;
    }
}
package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.util.SimpleTimer;

public class UnmodifiableClosingStage implements MatchClosingStage {

    private final MatchClosingStage delegate;

    public UnmodifiableClosingStage(MatchClosingStage delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getRemainingTime() {
        return delegate.getRemainingTime();
    }

    @Override
    public boolean isRunning() {
        return delegate.isRunning();
    }

    @Override
    public void start() throws UnsupportedOperationException {
        delegate.start();
    }

    @Override
    public void setMatch(Match match) {
        throw new UnsupportedOperationException("cannot overwrite match");
    }

    @Override
    public SimpleTimer getTimer() {
        return delegate.getTimer();
    }

    @Override
    public void run() {
        delegate.run();
    }
}
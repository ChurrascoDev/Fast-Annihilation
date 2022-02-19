package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.util.SimpleTimer;

public interface MatchClosingStage extends Runnable {

    int getRemainingTime();

    boolean isRunning();

    void start() throws UnsupportedOperationException;

    void setMatch(Match match);

    SimpleTimer getTimer();

}
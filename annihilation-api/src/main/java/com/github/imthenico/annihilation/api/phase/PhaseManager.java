package com.github.imthenico.annihilation.api.phase;

import com.github.imthenico.annihilation.api.util.SimpleTimer;

import java.util.Collection;

public interface PhaseManager extends Runnable {

    Collection<Integer> getPhases();

    void start() throws UnsupportedOperationException;

    RunnablePhase next() throws UnsupportedOperationException;

    boolean hasNext();

    SimpleTimer getTimer();

    RunnablePhase getCurrentPhase();

    boolean isLastPhase();

    abstract class RunnablePhase extends Phase {
        public RunnablePhase(Phase phase) {
            super(phase.getTotalTime(), phase.getPhaseNumber());
        }

        public abstract boolean isRunning();

        public abstract boolean finished();

    }
}
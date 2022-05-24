package com.github.imthenico.annihilation.api.phase;

import com.github.imthenico.annihilation.api.util.SimpleTimer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface PhaseManager extends Runnable {

    @NotNull Collection<Integer> getPhases();

    void start() throws UnsupportedOperationException;

    @NotNull RunnablePhase next() throws UnsupportedOperationException;

    boolean hasNext();

    @Nullable SimpleTimer getTimer();

    @Nullable RunnablePhase getCurrentPhase();

    boolean isLastPhase();

    abstract class RunnablePhase extends Phase {
        public RunnablePhase(int totalTime, int phaseNumber, PhaseAction phaseAction) {
            super(totalTime, phaseNumber, phaseAction);
        }

        public abstract boolean isRunning();

        public abstract boolean finished();

    }
}
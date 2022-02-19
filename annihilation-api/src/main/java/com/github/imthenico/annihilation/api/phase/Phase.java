package com.github.imthenico.annihilation.api.phase;

public class Phase {

    private final int totalTime;
    private final int phaseNumber;

    public Phase(
            int totalTime,
            int phaseNumber
    ) {
        this.totalTime = totalTime;
        this.phaseNumber = phaseNumber;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }
}
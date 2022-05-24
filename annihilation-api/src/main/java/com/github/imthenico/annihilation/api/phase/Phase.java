package com.github.imthenico.annihilation.api.phase;

public class Phase {

    private final int totalTime;
    private final int phaseNumber;
    private final PhaseAction phaseAction;

    public Phase(int totalTime, int phaseNumber, PhaseAction phaseAction) {
        this.totalTime = totalTime;
        this.phaseNumber = phaseNumber;
        this.phaseAction = phaseAction;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public PhaseAction getPhaseAction() {
        return phaseAction;
    }
}
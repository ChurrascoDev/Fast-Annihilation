package com.github.imthenico.annihilation.api.phase;

public interface PhaseExpansion {

    int[] supportedPhases();

    PhaseActionFactory getPhaseActionFactory();

}
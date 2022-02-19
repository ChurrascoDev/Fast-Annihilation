package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.simplecommons.util.Validate;

public class SimpleTimer {

    private final int totalTime;
    private int elapsedTime;

    public SimpleTimer(int totalTime) {
        this.totalTime = Validate.isTrue(totalTime >= 0, "totalUnits < 0", totalTime);
        this.elapsedTime = 0;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void elapse(int secondsToElapse, boolean restart) {
        Validate.isTrue(secondsToElapse >= 0, "toElapse < 0");

        elapsedTime += secondsToElapse;

        boolean ended = elapsedTime >= totalTime;

        if (ended && restart) {
            elapsedTime = totalTime;
        }
    }

    public void restart() {
        this.elapsedTime = 0;
    }

    public boolean isOver() {
        return elapsedTime == totalTime;
    }
}
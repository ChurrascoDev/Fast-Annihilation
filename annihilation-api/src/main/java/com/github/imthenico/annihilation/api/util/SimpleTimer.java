package com.github.imthenico.annihilation.api.util;

public class SimpleTimer {

    private final int totalTime;
    private int elapsedTime;

    public SimpleTimer(int totalTime) {
        if (totalTime < 0)
            throw new IllegalStateException("totalUnits < 0");

        this.totalTime = totalTime;
        this.elapsedTime = 0;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void elapse(int secondsToElapse, boolean restart) {
        if (secondsToElapse < 0)
            throw new IllegalStateException("secondsToElapse < 0");

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
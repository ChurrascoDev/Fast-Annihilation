package com.github.imthenico.annihilation.api.task;

public interface Schedulable extends Runnable {

    void stop();

    void resume();

    boolean running();

}
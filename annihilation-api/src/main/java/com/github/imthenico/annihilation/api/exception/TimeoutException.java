package com.github.imthenico.annihilation.api.exception;

import java.time.Duration;

public class TimeoutException extends java.util.concurrent.TimeoutException {

    private final Duration time;

    public TimeoutException(Duration time) {
        this.time = time;
    }

    public TimeoutException(String message, Duration time) {
        super(message);
        this.time = time;
    }

    public Duration getTime() {
        return time;
    }
}
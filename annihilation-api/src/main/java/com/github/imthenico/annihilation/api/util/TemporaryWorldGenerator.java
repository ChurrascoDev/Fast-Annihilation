package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.world.SimpleWorld;
import com.github.imthenico.annihilation.api.world.WorldTemplate;

import java.util.concurrent.atomic.AtomicInteger;

public final class TemporaryWorldGenerator {

    private static volatile TemporaryWorldGenerator INSTANCE;

    private TemporaryWorldGenerator() {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("Cannot re instantiate this class");
        }
    }

    private static final AtomicInteger GENERATION_COUNT = new AtomicInteger();

    public synchronized SimpleWorld generate(WorldTemplate worldTemplate) {
        int id = GENERATION_COUNT.getAndIncrement();
        String name = worldTemplate.getSource().getName();

        return SimpleWorld.generate(worldTemplate, id + "_" + name);
    }

    public int getCount() {
        return GENERATION_COUNT.get();
    }

    public static TemporaryWorldGenerator getInstance() {
        if (INSTANCE == null) {
            synchronized (TemporaryWorldGenerator.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemporaryWorldGenerator();
                }
            }
        }

        return INSTANCE;
    }
}
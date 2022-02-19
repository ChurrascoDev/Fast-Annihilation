package com.github.imthenico.annihilation.api.phase;

import com.github.imthenico.annihilation.api.event.PhaseEndEvent;
import com.github.imthenico.annihilation.api.event.PhaseStartEvent;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.simplecommons.util.Pair;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultPhaseManager implements PhaseManager {

    private final GameInstance game;
    private final Map<Integer, Pair<SimpleRunningPhase, PhaseAction>> phases;
    private final PluginManager pluginManager;

    private int currentPhase = 0;
    private SimpleTimer timer;

    private DefaultPhaseManager(
            GameInstance game,
            Map<Integer, Pair<SimpleRunningPhase, PhaseAction>> phases
    ) {
        this.phases = phases;
        this.game = game;
        this.pluginManager = Bukkit.getPluginManager();
    }

    @Override
    public Collection<Integer> getPhases() {
        return Collections.unmodifiableCollection(phases.keySet());
    }

    @Override
    public SimpleTimer getTimer() {
        return timer;
    }

    @Override
    public RunnablePhase getCurrentPhase() {
        return phases.get(currentPhase).getLeft();
    }

    @Override
    public boolean isLastPhase() {
        return currentPhase >= phases.size();
    }

    @Override
    public RunnablePhase next() throws UnsupportedOperationException {
        Validate.isTrue(hasNext(), "The last phase is running.");

        initPhase(currentPhase++);
        return phases.get(currentPhase).getLeft();
    }

    @Override
    public boolean hasNext() {
        return !isLastPhase();
    }

    @Override
    public void start() throws IllegalArgumentException {
        Validate.isTrue(currentPhase <= 0, "Manager is already started.");

        next();
    }

    private void initPhase(int phaseNumber) {
        Pair<SimpleRunningPhase, PhaseAction> phaseEntry = phases.get(phaseNumber);
        SimpleRunningPhase phase = phaseEntry.getLeft();

        if (currentPhase > 0) {
            endPhase(phase);
        }

        this.timer = new SimpleTimer(phase.getTotalTime());

        this.currentPhase = phaseNumber;
        phase.running = true;
        phaseEntry.getRight().accept(phase, game);

        pluginManager.callEvent(new PhaseStartEvent(phase, game.getMatch()));
    }

    private void endPhase(SimpleRunningPhase phase) {
        this.timer = null;

        phase.finished = true;
        phase.running = false;
        pluginManager.callEvent(new PhaseEndEvent(phase, game.getMatch()));
    }

    public static DefaultPhaseManager createDefaultPhaseManager(
            GameInstance game,
            Function<Integer, Integer> phaseDurationProvider,
            PhaseActionFactory phaseActionFactory,
            int... phases
    ) {
        Validate.isTrue(phases.length > 0, "phases <= 0");

        DefaultPhaseManager phaseManager = new DefaultPhaseManager(
                game,
                new LinkedHashMap<>(phases.length)
        );

        for (int phaseNumber : phases) {
            Phase phase = new Phase(phaseDurationProvider.apply(phaseNumber), phaseNumber);
            phaseManager.phases.put(phaseNumber, new Pair<>(new SimpleRunningPhase(phase), phaseActionFactory.newActionFor(phaseNumber)));
        }

        return phaseManager;
    }

    @Override
    public void run() {
        if (timer != null) {
            this.timer.elapse(1, true);
        }
    }

    private static class SimpleRunningPhase extends RunnablePhase {
        boolean running;
        boolean finished;

        public SimpleRunningPhase(Phase phase) {
            super(phase);
        }

        @Override
        public boolean isRunning() {
            return running;
        }

        @Override
        public boolean finished() {
            return finished;
        }
    }
}
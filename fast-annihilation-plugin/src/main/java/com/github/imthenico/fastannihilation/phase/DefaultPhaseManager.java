package com.github.imthenico.fastannihilation.phase;

import com.github.imthenico.annihilation.api.event.match.PhaseEndEvent;
import com.github.imthenico.annihilation.api.event.match.PhaseStartEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.phase.Phase;
import com.github.imthenico.annihilation.api.phase.PhaseAction;
import com.github.imthenico.annihilation.api.phase.PhaseActionFactory;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.util.Pair;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class DefaultPhaseManager implements PhaseManager {

    private final Game game;
    private final Map<Integer, Pair<SimpleRunningPhase, PhaseAction>> phases;
    private final PluginManager pluginManager;

    private int currentPhase = 0;
    private SimpleTimer timer;

    private DefaultPhaseManager(
            Game game,
            Map<Integer, Pair<SimpleRunningPhase, PhaseAction>> phases
    ) {
        this.phases = phases;
        this.game = game;
        this.pluginManager = Bukkit.getPluginManager();
    }

    @Override
    public @NotNull Collection<Integer> getPhases() {
        return Collections.unmodifiableCollection(phases.keySet());
    }

    @Override
    public @Nullable SimpleTimer getTimer() {
        return timer;
    }

    @Override
    public @Nullable RunnablePhase getCurrentPhase() {
        return phases.get(currentPhase).getLeft();
    }

    @Override
    public boolean isLastPhase() {
        return currentPhase >= phases.size();
    }

    @Override
    public @NotNull RunnablePhase next() throws UnsupportedOperationException {
        if (!hasNext())
            throw new UnsupportedOperationException("The last phase is running.");

        initPhase(currentPhase);
        return phases.get(currentPhase).getLeft();
    }

    @Override
    public boolean hasNext() {
        return !isLastPhase();
    }

    @Override
    public void start() throws UnsupportedOperationException {
        if (currentPhase >= 0)
            throw new UnsupportedOperationException("Manager is already started");

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

        pluginManager.callEvent(new PhaseStartEvent(game.runningMatch(), phase));
    }

    private void endPhase(SimpleRunningPhase phase) {
        this.timer = null;

        phase.finished = true;
        phase.running = false;
        pluginManager.callEvent(new PhaseEndEvent(game.runningMatch(), phase));
    }

    public static DefaultPhaseManager createDefaultPhaseManager(
            Game game,
            Function<Integer, Integer> phaseDurationProvider,
            PhaseActionFactory phaseActionFactory,
            int... phases
    ) {
        if (phases.length <= 0) {
            throw new IllegalArgumentException("phases <= 0");
        }

        DefaultPhaseManager phaseManager = new DefaultPhaseManager(
                game,
                new LinkedHashMap<>(phases.length)
        );

        for (int phaseNumber : phases) {
            Integer duration = phaseDurationProvider.apply(phaseNumber);
            Objects.requireNonNull(duration, "duration of " + phaseNumber + " is null");

            PhaseAction phaseAction = phaseActionFactory.newActionFor(phaseNumber);
            Objects.requireNonNull(phaseAction, "phase action of " + phaseNumber + " is null");

            phaseManager.phases.put(phaseNumber, new Pair<>(new SimpleRunningPhase(duration, phaseNumber, phaseAction), phaseAction));
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

        public SimpleRunningPhase(int totalTime, int phaseNumber, PhaseAction phaseAction) {
            super(totalTime, phaseNumber, phaseAction);
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
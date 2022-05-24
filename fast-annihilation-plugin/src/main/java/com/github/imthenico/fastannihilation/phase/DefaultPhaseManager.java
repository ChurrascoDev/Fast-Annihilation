package com.github.imthenico.fastannihilation.phase;

import com.github.imthenico.annihilation.api.event.match.PhaseEndEvent;
import com.github.imthenico.annihilation.api.event.match.PhaseStartEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.phase.PhaseAction;
import com.github.imthenico.annihilation.api.phase.PhaseActionFactory;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class DefaultPhaseManager implements PhaseManager {

    private final Game game;
    private final Queue<SimpleRunningPhase> phases;
    private final List<Integer> phaseNumbers;
    private final PluginManager pluginManager;

    private int currentPhase = 0;
    private SimpleTimer timer;

    private DefaultPhaseManager(
            Game game,
            Queue<SimpleRunningPhase> phases
    ) {
        this.phases = phases;
        this.phaseNumbers = new ArrayList<>();

        phases.forEach(phase -> phaseNumbers.add(phase.getPhaseNumber()));

        this.game = game;
        this.pluginManager = Bukkit.getPluginManager();
    }

    @Override
    public @NotNull Collection<Integer> getPhases() {
        return Collections.unmodifiableCollection(phaseNumbers);
    }

    @Override
    public @Nullable SimpleTimer getTimer() {
        return timer;
    }

    @Override
    public @Nullable RunnablePhase getCurrentPhase() {
        return phases.peek();
    }

    @Override
    public boolean isLastPhase() {
        return currentPhase >= (phases.size() - 1);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public @NotNull RunnablePhase next() throws UnsupportedOperationException {
        if (!hasNext())
            throw new UnsupportedOperationException("The last phase is running.");

        SimpleRunningPhase prev = phases.peek();
        endPhase(prev);
        SimpleRunningPhase runningPhase = phases.poll();
        initPhase(runningPhase);

        return phases.peek();
    }

    @Override
    public boolean hasNext() {
        return !isLastPhase();
    }

    @Override
    public void start() throws UnsupportedOperationException {
        if (currentPhase > 0)
            throw new UnsupportedOperationException("Manager is already started");

        next();
    }

    private void initPhase(SimpleRunningPhase phase) {
        this.timer = new SimpleTimer(phase.getTotalTime());

        this.currentPhase = phase.getPhaseNumber();
        phase.running = true;
        phase.getPhaseAction().accept(phase, game);

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

        Arrays.sort(phases);

        Queue<SimpleRunningPhase> runningPhases = new LinkedList<>();
        for (int phaseNumber : phases) {
            Integer duration = phaseDurationProvider.apply(phaseNumber);
            Objects.requireNonNull(duration, "duration of " + phaseNumber + " is null");

            PhaseAction phaseAction = phaseActionFactory.newActionFor(phaseNumber);
            Objects.requireNonNull(phaseAction, "phase action of " + phaseNumber + " is null");

            runningPhases.add(new SimpleRunningPhase(duration, phaseNumber, phaseAction));
        }

        return new DefaultPhaseManager(game, runningPhases);
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
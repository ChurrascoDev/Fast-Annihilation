package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.simplecommons.util.Validate;

public class DefaultMatchClosingStage extends AbstractMatchClosingStage {

    private final SimpleTimer timer;
    private boolean running;

    public DefaultMatchClosingStage(int totalTime) {
        this.timer = new SimpleTimer(totalTime);
    }

    @Override
    public int getRemainingTime() {
        return timer.getTotalTime() - timer.getElapsedTime();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void start() {
        Match match = match();

        Validate.notNull(match, "match");

        if (!match.allPhasesFinished() || running) {
            throw new UnsupportedOperationException();
        }

        this.running = true;
    }

    @Override
    public SimpleTimer getTimer() {
        return timer;
    }

    @Override
    public void run() {
        Match match = match();

        if (match == null)
            return;

        GameInstance game = match.getGame();

        for (AnniPlayer player : game.getPlayers((p) -> true)) {
            player.getPlayer().sendMessage(String.format("The match will end in: %s", getRemainingTime()));
        }

        timer.elapse(1, false);
    }
}
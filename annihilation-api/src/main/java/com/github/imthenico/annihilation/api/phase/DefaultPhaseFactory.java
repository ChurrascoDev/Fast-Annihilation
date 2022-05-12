package com.github.imthenico.annihilation.api.phase;

import com.github.imthenico.annihilation.api.match.Match;
import java.util.Objects;
import me.yushust.message.MessageHandler;

public class DefaultPhaseFactory implements PhaseActionFactory {

    private final MessageHandler messageHandler;

    public DefaultPhaseFactory(MessageHandler messageHandler) {
        this.messageHandler = Objects.requireNonNull(messageHandler);
    }

    @Override
    public PhaseAction newActionFor(int phase) {
        switch (phase) {
            case 1:
                return firstPhase();
            case 2:
                return secondPhase();
            case 3:
                return thirdPhase();
            case 4:
                return fourthPhase();
            case 5:
                return fifthPhase();
        }

        throw new IllegalArgumentException("Unsupported Phase: " + phase);
    }

    private PhaseAction firstPhase() {
        return (phase, game) -> {
            game.getOptions().setNexusInvulnerability(true);

            messageHandler.send(game, "broadcast.first-phase");
        };
    }

    private PhaseAction secondPhase() {
        return (phase, game) -> {
            game.getOptions().setNexusInvulnerability(false);

            messageHandler.send(game, "broadcast.second-phase");
        };
    }

    private PhaseAction thirdPhase() {
        return (phase, game) -> {
            // spawn diamonds

            messageHandler.send(game, "broadcast.third-phase");
        };
    }

    private PhaseAction fourthPhase() {
        return (phase, game) -> {
            Match match = game.runningMatch();

            /*
            for (Boss<?> boss : environment.getBosses().values()) {
                if (boss.isSpawned()) {
                    boss.spawn();
                }
            }
             */

            messageHandler.send(game, "broadcast.fourth-phase");
        };
    }

    private PhaseAction fifthPhase() {
        return (phase, game) -> {
            game.getOptions().setNexusDamageMultiplier(2);

            messageHandler.send(game, "broadcast.fifth-phase");
        };
    }
}
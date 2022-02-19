package com.github.imthenico.annihilation.api.task;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.message.MessagePath;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.util.GameValidation;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.simplecommons.bukkit.util.TextColorApplier;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public class GameTimerUpdater extends BukkitRunnable {

    private final GameInstanceManager gameInstances;
    private final MessageHandler messageHandler;

    public GameTimerUpdater(
            GameInstanceManager gameInstances,
            MessageHandler messageHandler
    ) {
        this.gameInstances = gameInstances;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        if (!TaskStateProvider.isRunning(this))
            return;

        for (GameInstance instance : gameInstances) {
            Match match = instance.getMatch();

            if (match != null) {
                handleMatchTimer(match);
                continue;
            }

            handleTimer(instance);
        }
    }

    private void handleMatchTimer(Match match) {
        if (!TaskStateProvider.isRunning(this))
            return;

        if (match.finalized()) {
            cancel();
            return;
        }

        MatchClosingStage ending = match.getClosingStage();

        if (ending.isRunning()) {
            if (ending.getRemainingTime() > 0) {
                ending.run();
            } else {
                match.getGame().discardMatch();
            }
        } else {
            if (match.allPhasesFinished()) {
                ending.start();
                return;
            }

            match.getPhaseManager().run();
        }
    }

    private void handleTimer(GameInstance gameInstance) {
        PreMatchStage preMatchStage = gameInstance.getPreparationStage();

        SimpleTimer timer = preMatchStage.getCountdownToStart();

        List<AnniPlayer> players = gameInstance.getPlayers((a) -> true);

        if (timer.isOver()) {
            AuthorizationResult authorizationResult = gameInstance.startMatch();

            if (!authorizationResult.isAuthorized()) {
                MessagePath path = authorizationResult.getReasonMessage();
                String messagePath = path.getMessagePath();

                if (messagePath != null) {
                    messageHandler.sendReplacing(
                            players,
                            messagePath
                    );
                } else {
                    for (AnniPlayer player : players) {
                        player.getPlayer().sendMessage(
                                TextColorApplier.color(path.getDefaultMessage())
                        );
                    }
                }

                String reason = authorizationResult.getReason();

                if (reason != null) {
                    Bukkit.getLogger().log(Level.WARNING, "Match cannot start, reason: " + reason);
                }
            }

            cancel();
            return;
        }

        boolean startedToCountdown = timer.getElapsedTime() > 0;

        if (GameValidation.balancedTeams(gameInstance)) {
            if (!startedToCountdown) {
                messageHandler
                        .send(players, "starting-game");
            }

            int remainingTime = timer.getTotalTime() - timer.getElapsedTime();
            messageHandler
                    .send(players, "game-start-in-" + remainingTime);

            timer.elapse(1, false);
        } else {
            if (startedToCountdown) {
                messageHandler
                        .send(players, "countdown-insufficient-players");

                timer.restart();
            }
        }
    }
}
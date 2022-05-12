package com.github.imthenico.fastannihilation.task;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.game.GameManager;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.message.MessagePath;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.annihilation.api.util.GameValidation;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.annihilation.api.util.TaskStateProvider;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.logging.Level;

public class GameTimerUpdater extends BukkitRunnable {

    private final GameManager gameInstances;
    private final MessageHandler messageHandler;

    public GameTimerUpdater(
            GameManager gameInstances,
            MessageHandler messageHandler
    ) {
        this.gameInstances = gameInstances;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        if (!TaskStateProvider.isRunning(this))
            return;

        for (GameRoom room : gameInstances) {
            if (!room.isEnabled()) {
                continue;
            }

            Match match = room.game().runningMatch();

            if (match != null) {
                handleMatchTimer(match);
                continue;
            }

            handleTimer(room);
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
                match.getGame().room().restoreLogic();
            }
        } else {
            if (match.allPhasesFinished()) {
                ending.start();
                return;
            }

            match.getPhaseManager().run();
        }
    }

    private void handleTimer(GameRoom room) {
        Game game = room.game();

        PreMatchStage preMatchStage = game.getPreparationStage();

        SimpleTimer timer = preMatchStage.getCountdownToStart();

        List<AnniPlayer> players = room.getPlayers((p) -> true);

        if (timer.isOver()) {
            AuthorizationResult authorizationResult = game.startMatch();

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
                                Formatting.colorize(path.getDefaultMessage())
                        );
                    }
                }

                String reason = authorizationResult.getReason();

                if (reason != null) {
                    Bukkit.getLogger().log(Level.WARNING, "Match cannot start, reason: " + reason);
                }
            }

            return;
        }

        boolean startedToCountdown = timer.getElapsedTime() > 0;

        if (GameValidation.balancedTeams(game)) {
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
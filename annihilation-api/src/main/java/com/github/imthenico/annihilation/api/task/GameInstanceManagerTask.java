package com.github.imthenico.annihilation.api.task;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import org.bukkit.scheduler.BukkitRunnable;

public class GameInstanceManagerTask extends BukkitRunnable {

    private final GameInstanceManager gameInstanceManager;

    public GameInstanceManagerTask(
            GameInstanceManager gameInstanceManager
    ) {
        this.gameInstanceManager = gameInstanceManager;
    }

    @Override
    public void run() {
        for (GameInstance gameInstance : gameInstanceManager) {
            PreMatchStage preMatchStage = gameInstance.getPreparationStage();

            preMatchStage.getCountdownToStart().elapse(1, true);
        }
    }
}
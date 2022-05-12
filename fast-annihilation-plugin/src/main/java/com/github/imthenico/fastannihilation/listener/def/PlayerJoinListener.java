package com.github.imthenico.fastannihilation.listener.def;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import net.hexaway.board.abstraction.ComplexBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final PlayerRegistry playerRegistry;
    private final ScoreboardService scoreboardService;

    public PlayerJoinListener(
            PlayerRegistry playerRegistry,
            ScoreboardService scoreboardService
    ) {
        this.playerRegistry = playerRegistry;
        this.scoreboardService = scoreboardService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void registerPlayerModelOnJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!playerRegistry.exists(player.getUniqueId())) {
            ComplexBoard complexBoard = scoreboardService.displayBoard(player);

            playerRegistry.registerPlayer(new AnniPlayer(player, complexBoard));
        }
    }

    @EventHandler
    public void registerScoreboardOnJoin(PlayerJoinEvent event) {
        scoreboardService.displayBoard(event.getPlayer());
    }
}
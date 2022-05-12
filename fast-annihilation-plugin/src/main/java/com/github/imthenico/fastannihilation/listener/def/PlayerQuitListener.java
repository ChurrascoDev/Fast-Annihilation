package com.github.imthenico.fastannihilation.listener.def;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final ModelSetupManager modelSetupManager;
    private final PlayerRegistry playerRegistry;

    public PlayerQuitListener(ModelSetupManager modelSetupManager, PlayerRegistry playerRegistry) {
        this.modelSetupManager = modelSetupManager;
        this.playerRegistry = playerRegistry;
    }

    @EventHandler
    public void handleQuitOnSetup(PlayerQuitEvent event) {
        AnniPlayer anniPlayer = playerRegistry.getPlayer(event.getPlayer().getUniqueId());

        modelSetupManager.removePlayerFromSession(anniPlayer);
    }
}
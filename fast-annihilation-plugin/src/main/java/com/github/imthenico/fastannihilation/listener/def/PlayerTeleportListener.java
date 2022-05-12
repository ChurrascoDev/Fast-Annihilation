package com.github.imthenico.fastannihilation.listener.def;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.annihilation.api.editor.SetupContext;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldHolder;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class PlayerTeleportListener implements Listener {

    private final ModelSetupManager modelSetupManager;

    public PlayerTeleportListener(ModelSetupManager modelSetupManager) {
        this.modelSetupManager = modelSetupManager;
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        SetupContext<?> setupContext = modelSetupManager.getSession(event.getPlayer());

        if (setupContext == null)
            return;

        WorldHolder worldHolder = setupContext.getEditingTarget();

        UUID worldId = event.getTo().getWorld().getUID();
        AWorld aWorld = worldHolder.allWorlds().getById(worldId);

        if (aWorld == null) {
            event.setCancelled(true);
            event.getPlayer()
                    .sendMessage(ChatColor.RED + "You're editing a model, type /model leave if you want to teleport another world.");
        }
    }
}
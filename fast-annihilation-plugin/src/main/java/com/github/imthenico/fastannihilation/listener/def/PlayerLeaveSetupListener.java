package com.github.imthenico.fastannihilation.listener.def;

import com.github.imthenico.annihilation.api.event.setup.PlayerLeaveSetupEvent;
import com.github.imthenico.annihilation.api.world.LocationReference;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeaveSetupListener implements Listener {

    private final LocationReference hubSpawnReference;

    public PlayerLeaveSetupListener(LocationReference hubSpawnReference) {
        this.hubSpawnReference = hubSpawnReference;
    }

    @EventHandler
    public void onLeave(PlayerLeaveSetupEvent event) {
        Player player = event.getPlayer().getPlayer();
        Location location = hubSpawnReference.get();

        player.teleport(location);
        player.sendMessage("Teleporting to lobby...");
    }
}
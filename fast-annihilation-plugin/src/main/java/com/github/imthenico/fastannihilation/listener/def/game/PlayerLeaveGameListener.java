package com.github.imthenico.fastannihilation.listener.def.game;

import com.github.imthenico.annihilation.api.event.game.PlayerLeaveRoomEvent;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.fastannihilation.service.ScoreboardServiceImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeaveGameListener implements Listener {

    @EventHandler
    public void onLeave(PlayerLeaveRoomEvent event) {
        AnniPlayer anniPlayer = event.getPlayer();

        ScoreboardServiceImpl.LOBBY_BOARD_MODEL.install(anniPlayer.getComplexBoard());
    }
}
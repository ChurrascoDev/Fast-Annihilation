package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.event.match.MatchStartEvent;
import com.github.imthenico.fastannihilation.service.ScoreboardServiceImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MatchStartListener implements Listener {

    @EventHandler
    public void onStart(MatchStartEvent event) {
        event.getMatch()
                .getGame()
                .room()
                .getPlayers(anniPlayer -> true)
                .forEach(anniPlayer -> ScoreboardServiceImpl.GAME_BOARD_MODEL.install(anniPlayer.getComplexBoard()));
    }
}
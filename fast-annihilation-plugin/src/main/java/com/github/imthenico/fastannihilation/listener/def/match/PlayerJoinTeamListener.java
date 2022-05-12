package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.PlayerJoinTeamEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinTeamListener implements Listener {

    @EventHandler
    public void onTeamJoin(PlayerJoinTeamEvent event) {
        MatchPlayer matchPlayer = event.getMatchPlayer();
        matchPlayer.getPlayer().teleport(event.getNewTeam().getRandomSpawn());
    }
}
package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.event.game.PlayerSelectTeamEvent;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import me.yushust.message.MessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Locale;

public class PlayerSelectTeamListener implements Listener {

    private final MessageHandler messageHandler;

    public PlayerSelectTeamListener(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @EventHandler
    public void onSelect(PlayerSelectTeamEvent event) {
        TeamColor lastTeam = event.getLastTeam();
        TeamColor newTeam = event.getTeam();
        AnniPlayer player = event.getPlayer();

        if (lastTeam != null) {
            messageHandler.sendReplacing(
                    player,
                    "team-change",
                    "<last>",
                    lastTeam.getColorCode() + lastTeam.name().toLowerCase(Locale.ROOT),
                    "<team>",
                    newTeam.getColorCode() + newTeam.name().toLowerCase(Locale.ROOT)
            );
        } else {
            messageHandler.sendReplacing(
                    player,
                    "team-join",
                    "<team>",
                    newTeam.getColorCode() + newTeam.name().toLowerCase(Locale.ROOT)
            );
        }
    }
}
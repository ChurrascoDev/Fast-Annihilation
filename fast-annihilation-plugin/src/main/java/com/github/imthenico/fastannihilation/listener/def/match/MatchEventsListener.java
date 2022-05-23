package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.NexusDamageEvent;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.match.Match;
import me.yushust.message.MessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.github.imthenico.annihilation.api.util.Translations.*;

public class MatchEventsListener implements Listener {

    private final String expectedMatchTypeName;
    private final MessageHandler messageHandler;

    public MatchEventsListener(String expectedMatchTypeName, MessageHandler messageHandler) {
        this.expectedMatchTypeName = expectedMatchTypeName;
        this.messageHandler = messageHandler;
    }

    @EventHandler
    public void onNexusDamage(NexusDamageEvent event) {
        MatchPlayer matchPlayer = event.getExternalAgent();

        if (matchPlayer == null)
            return;

        Match match = matchPlayer.getMatch();

        if (!match.getTypeName().equals(expectedMatchTypeName)) {
            return;
        }

        Nexus nexus = event.getNexus();
        int damage = event.getDamage();

        messageHandler.sendReplacing(
                match,
                "game.nexus-damaged-by-player",
                "%nexus_color%", getColorTranslation(messageHandler, matchPlayer, nexus.color()),
                "%colored_player%", nexus.color().getColorCode() + matchPlayer.getHandle().getName(),
                "%damage%", damage
        );
    }
}
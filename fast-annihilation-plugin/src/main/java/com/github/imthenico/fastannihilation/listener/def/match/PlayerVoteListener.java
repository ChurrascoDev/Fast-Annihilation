package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.event.game.PlayerVoteMapEvent;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import me.yushust.message.MessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerVoteListener implements Listener {

    private final MessageHandler messageHandler;

    public PlayerVoteListener(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @EventHandler
    public void onPlayerVote(PlayerVoteMapEvent event) {
        AnniPlayer player = event.getVoter();
        String votedMap = event.getVote();
        String lastVoted = event.getLastVote();

        if (lastVoted != null) {
            messageHandler.sendReplacing(
                    player,
                    "map-vote-change",
                    "<last>",
                    lastVoted,
                    "<new>",
                    votedMap
            );
        } else {
            messageHandler.sendReplacing(
                    player,
                    "map-vote",
                    "<map>",
                    votedMap
            );
        }
    }
}
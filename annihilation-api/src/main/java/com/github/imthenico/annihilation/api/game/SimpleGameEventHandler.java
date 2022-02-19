package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;

import java.util.Locale;

public class SimpleGameEventHandler implements GameEventHandler {

    private final GameInstance game;
    private final MessageHandler messageHandler;

    public SimpleGameEventHandler(
            GameInstance game,
            MessageHandler messageHandler
    ) {
        this.game = Validate.notNull(game);
        this.messageHandler = Validate.notNull(messageHandler);
    }

    @Override
    public void handleRoomLeave(AnniPlayer player) {
        Match match;
        if ((match = game.getMatch()) != null) {
            MatchPlayer matchPlayer = match.getPlayer(player);

            if (matchPlayer != null) {
                match.leave(player);
            }
        }
    }

    @Override
    public void handleGameJoin(AnniPlayer player) {
        Match match;
        if ((match = game.getMatch()) != null) {
            MatchPlayer matchPlayer = match.getPlayer(player);

            if (matchPlayer == null || matchPlayer.isDisqualified()) {
                teleportToLobby(player);
            }
        }
    }

    @Override
    public void handleTeamJoin(AnniPlayer player, TeamColor lastTeam, TeamColor newTeam) {
        Validate.notNull(player);
        Validate.notNull(newTeam);

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

    @Override
    public void handleTeamLeave(AnniPlayer player, TeamColor team) {
        messageHandler.sendReplacing(
                player,
                "team-leave",
                "<team>",
                team.getColorCode() + team.name().toLowerCase(Locale.ROOT)
        );
    }

    @Override
    public void handleMapVote(AnniPlayer player, MatchMap lastVoted, ConfigurableModel votedMap) {
        if (lastVoted != null) {
            messageHandler.sendReplacing(
                    player,
                    "map-vote-change",
                    "<last>",
                    lastVoted.getName(),
                    "<new>",
                    votedMap.getDisplayName()
            );
        } else {
            messageHandler.sendReplacing(
                    player,
                    "map-vote",
                    "<map>",
                    votedMap.getDisplayName()
            );
        }
    }

    private void teleportToLobby(AnniPlayer player) {
        GameLobby lobby = game.getLobby();

        player.getPlayer().teleport(lobby.getWorld().getSpawnLocation().get());
    }
}
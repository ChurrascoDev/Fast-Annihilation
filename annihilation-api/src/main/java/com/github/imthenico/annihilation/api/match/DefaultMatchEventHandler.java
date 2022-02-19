package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.GameLobby;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.ingame.Nexus;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;

import java.util.Map;
import java.util.UUID;

import static com.github.imthenico.annihilation.api.util.Translations.*;

public class DefaultMatchEventHandler implements MatchEventHandler {

    private final MessageHandler messageHandler;

    public DefaultMatchEventHandler(MessageHandler messageHandler) {
        this.messageHandler = Validate.notNull(messageHandler);
    }

    @Override
    public void handleNexusDamage(MatchPlayer matchPlayer, int damage, Nexus nexus) {
        Match match = matchPlayer.getMatch();

        messageHandler.sendReplacing(
                match,
                "game.nexus-damaged-by-player",
                "%nexus_color%", getColorTranslation(messageHandler, matchPlayer, nexus.color()),
                "%colored_player%", nexus.color().getColorCode() + matchPlayer.getHandle().getName(),
                "%damage%", damage+""
        );
    }

    @Override
    public void handleNexusDeath(Nexus nexus) {

    }

    @Override
    public void handleMatchStart(Match match) {
        GameInstance gameInstance = match.getGame();
        PreMatchStage preMatchStage = gameInstance.getPreparationStage();

        Map<UUID, TeamColor> teamSelection = preMatchStage.getTeamSelection();

        for (AnniPlayer player : gameInstance.getPlayers((p) -> true)) {
            TeamColor color = teamSelection.get(player.getId());

            if (color == null)
                continue;

            match.join(player);
        }
    }

    @Override
    public void handleMatchEnd(MatchState matchState) {

    }
}
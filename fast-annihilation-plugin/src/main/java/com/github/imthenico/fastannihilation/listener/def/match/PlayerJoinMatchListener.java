package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.PlayerJoinMatchEvent;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import java.util.Objects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.UUID;

public class PlayerJoinMatchListener implements Listener {

    private final String expectedMatchTypeName;

    public PlayerJoinMatchListener(String expectedMatchTypeName) {
        this.expectedMatchTypeName = Objects.requireNonNull(expectedMatchTypeName);
    }

    @EventHandler
    public void onJoin(PlayerJoinMatchEvent event) {
        if (!event.isOfType(expectedMatchTypeName)) {
            return;
        }

        Match match = event.getMatch();
        MatchPlayer matchPlayer = event.getMatchPlayer();

        GameRoom gameRoom = match.getGame().room();
        PreMatchStage preMatchStage = gameRoom.game().getPreparationStage();

        Map<UUID, TeamColor> teamSelection = preMatchStage.getTeamSelection();

        TeamColor color = teamSelection.get(matchPlayer.getUniqueId());

        if (color == null)
            return;

        MatchTeam team = match.getRunningMap().getTeam(color);

        team.join(matchPlayer);
    }
}
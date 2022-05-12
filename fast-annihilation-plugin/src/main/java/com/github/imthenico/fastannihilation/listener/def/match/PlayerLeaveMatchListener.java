package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.game.PlayerLeaveRoomEvent;
import com.github.imthenico.annihilation.api.event.match.PlayerLeaveMatchEvent;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import java.util.Objects;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeaveMatchListener implements Listener {

    private final String expectedMatchTypeName;

    public PlayerLeaveMatchListener(String expectedMatchTypeName) {
        this.expectedMatchTypeName = Objects.requireNonNull(expectedMatchTypeName);
    }

    @EventHandler
    public void onLeave(PlayerLeaveMatchEvent event) {
        if (!event.isOfType(expectedMatchTypeName)) {
            return;
        }

        tryDisqualify(event.getMatch(), event.getMatchPlayer());
    }

    @EventHandler
    public void onRoomLeave(PlayerLeaveRoomEvent event) {
        GameRoom room = event.getRoom();
        Match match = room.game().runningMatch();

        if (match != null) {
            tryDisqualify(match, event.getPlayer());
        }
    }

    private void tryDisqualify(Match match, Object player) {
        MatchPlayer matchPlayer = player instanceof MatchPlayer ? (MatchPlayer) player : match.getPlayer((AnniPlayer) player);

        MatchTeam team = matchPlayer.getTeam();

        if (team != null && team.isDeath() && !matchPlayer.isDisqualified()) {
            matchPlayer.setDisqualified(true);
        }
    }
}
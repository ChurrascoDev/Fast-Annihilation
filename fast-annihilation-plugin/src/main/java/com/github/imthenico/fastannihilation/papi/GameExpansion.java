package com.github.imthenico.fastannihilation.papi;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.Locale;

public class GameExpansion extends PlaceholderExpansion {

    private final PlayerRegistry playerRegistry;

    public GameExpansion(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        AnniPlayer anniPlayer = playerRegistry.getPlayer(p.getUniqueId());

        if (anniPlayer == null)
            return null;

        Game game = anniPlayer.getPlayingGame();

        if (game == null)
            return null;

        PreMatchStage preMatchStage = game.getPreparationStage();
        switch (params) {
            case "time": {
                SimpleTimer timer = preMatchStage.getCountdownToStart();

                return Formatting.formatSeconds(timer.getElapsedTime(), "%s:%s:%s");
            }
            case "id": {
                return game.room().id();
            }
            case "players": {
                return game.room().playerCount() + "";
            }
            case "min_players": {
                return (game.getRules().getMinPlayersPerTeam() * 4) + "";
            }
        }

        if (params.startsWith("team")) {
            String[] separated = params.split("_");

            if (separated.length < 3)
                return null;

            String teamName = separated[1];

            try {
                TeamColor color = TeamColor.valueOf(teamName.toUpperCase(Locale.ROOT));

                String requested = separated[2];

                return getRequestedTeamData(preMatchStage, color, requested);
            } catch (Exception ignored) {}
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "game";
    }

    @Override
    public String getAuthor() {
        return "ImTheNico_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    private String getRequestedTeamData(PreMatchStage preMatchStage, TeamColor color, String requested) {
        switch (requested) {
            case "count": {
                int assertions = 0;
                for (TeamColor value : preMatchStage.getTeamSelection().values()) {
                    if (color == value)
                        assertions++;
                }

                return assertions + "";
            }
        }

        return null;
    }
}
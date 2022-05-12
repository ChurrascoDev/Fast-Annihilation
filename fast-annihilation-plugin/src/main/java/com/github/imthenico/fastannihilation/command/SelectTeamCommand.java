package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Command(names = "team")
public class SelectTeamCommand implements CommandClass {

    private final Map<String, TeamColor> colorMap = new HashMap<>();

    public SelectTeamCommand() {
        for (TeamColor value : TeamColor.values()) {
            colorMap.put(value.name().toLowerCase(Locale.ROOT), value);
        }
    }

    @Command(names = "")
    public boolean rootCommand(@Sender AnniPlayer anniPlayer, String colorName) {
        GameRoom gameRoom = anniPlayer.getPlayingRoom();

        if (gameRoom == null) {
            anniPlayer.sendMessage("&cYou are not in any room.", true);
            return true;
        }

        TeamColor teamColor = colorMap.get(colorName);

        if (teamColor == null) {
            anniPlayer.sendMessage("&cInvalid team name", true);
            return true;
        }

        PreMatchStage preMatchStage = gameRoom.game().getPreparationStage();

        int teamSize = 0;

        for (TeamColor value : preMatchStage.getTeamSelection().values()) {
            if (teamColor == value) {
                teamSize++;
            }
        }

        Player player = anniPlayer.getPlayer();
        if (teamSize >= gameRoom.getRules().getMaxPlayersPerTeam() && !player.hasPermission("anni.bypass.team-limit")) {
            anniPlayer.sendMessage("&cThis team is full", true);
            return true;
        }

        preMatchStage.joinTeam(anniPlayer, teamColor);
        return true;
    }
}
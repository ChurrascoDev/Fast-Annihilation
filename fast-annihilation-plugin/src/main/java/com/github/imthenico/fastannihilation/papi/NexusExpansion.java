package com.github.imthenico.fastannihilation.papi;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class NexusExpansion extends PlaceholderExpansion {

    private final PlayerRegistry registry;

    public NexusExpansion(PlayerRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        AnniPlayer anniPlayer = registry.getPlayer(p.getUniqueId());
        if (anniPlayer == null)
            return null;

        MatchPlayer matchPlayer = MatchPlayer.from(anniPlayer);
        if (matchPlayer == null)
            return null;

        int firstParamSeparatorIndex = params.indexOf("_");

        if (firstParamSeparatorIndex < 0 || firstParamSeparatorIndex + 1 >= params.length())
            return null;

        String teamName = params.substring(0, firstParamSeparatorIndex);
        TeamColor color = TeamColor.byName(teamName);
        if (color == null)
            return null;

        MatchMap matchMap = matchPlayer
                .getMatch()
                .getRunningMap();

        MatchTeam matchTeam = matchMap.getTeam(color);

        String request = params.substring(firstParamSeparatorIndex + 1);
        Nexus nexus = matchTeam.getNexus();

        switch (request) {
            case "health":
                return nexus.getHealth() + "";
            case "elegant_health": {
                int nexusHealth = nexus.getHealth();

                if (nexusHealth <= 0)
                    return "&c✘";

                return nexusHealth + "";
            }
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "nexus";
    }

    @Override
    public String getAuthor() {
        return "ImTheNico_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

}
package com.github.imthenico.fastannihilation.papi;

import com.github.imthenico.annihilation.api.team.TeamColor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TeamExpansion extends PlaceholderExpansion {

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        String[] separated = params.split("_");

        if (separated.length < 2) return null;

        String teamName = separated[0];

        try {
            TeamColor color = TeamColor.valueOf(teamName);

            String request = separated[1];

            switch (request) {
                case "colored":
                    return color.getColoredName();
                case "color":
                    return color.getColorCode().toString();
                case "name":
                case "translate":
                    return color.name();
            }
        } catch (Exception ignored) {}

        return null;
    }

    @Override
    public String getIdentifier() {
        return "team";
    }

    @Override
    public String getAuthor() {
        return "ImTheNico_";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
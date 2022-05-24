package com.github.imthenico.annihilation.api.team;

import org.bukkit.ChatColor;

import java.util.*;

public enum TeamColor {

    RED(ChatColor.RED),
    YELLOW(ChatColor.YELLOW),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN);

    private final static Collection<TeamColor> VALUES = Arrays.asList(values());

    private final ChatColor colorCode;

    TeamColor(ChatColor colorCode) {
        this.colorCode = colorCode;
    }

    public ChatColor getColorCode() {
        return colorCode;
    }

    public String getColoredName() {
        return colorCode + name();
    }

    public String getLowerCaseName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static TeamColor byName(String name) {
        for (TeamColor value : VALUES) {
            if (value.name().equalsIgnoreCase(name)) return value;
        }
        return null;
    }
}
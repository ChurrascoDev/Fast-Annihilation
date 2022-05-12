package com.github.imthenico.annihilation.api.team;

import org.bukkit.ChatColor;

import java.util.Locale;

public enum TeamColor {

    RED(ChatColor.RED),
    YELLOW(ChatColor.YELLOW),
    BLUE(ChatColor.BLUE),
    GREEN(ChatColor.GREEN);

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
}
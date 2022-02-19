package com.github.imthenico.annihilation.api.team;

public enum TeamColor {

    RED("&c"),
    YELLOW("&e"),
    BLUE("&9"),
    GREEN("&a");

    private final String colorCode;

    TeamColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}
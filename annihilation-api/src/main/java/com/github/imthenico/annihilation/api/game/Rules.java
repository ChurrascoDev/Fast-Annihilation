package com.github.imthenico.annihilation.api.game;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    private int timeToStart;
    private int timeToEnd;
    private int maxPlayers;
    private int minPlayersPerTeam;
    private int maxPlayersPerTeam;
    private final List<String> allowedMaps;
    private String defaultMap;

    public Rules(
            int timeToStart,
            int timeToEnd,
            int maxPlayers,
            int minPlayersPerTeam,
            int maxPlayersPerTeam,
            List<String> allowedMaps,
            String defaultMap
    ) {
        this.timeToStart = timeToStart;
        this.timeToEnd = timeToEnd;
        this.maxPlayers = maxPlayers;
        this.minPlayersPerTeam = minPlayersPerTeam;
        this.maxPlayersPerTeam = maxPlayersPerTeam;
        this.allowedMaps = allowedMaps != null ? new ArrayList<>(allowedMaps) : new ArrayList<>();
        this.defaultMap = defaultMap;
    }

    public int getTimeToStart() {
        return timeToStart;
    }

    public void setTimeToStart(int timeToStart) {
        this.timeToStart = timeToStart;
    }

    public int getTimeToEnd() {
        return timeToEnd;
    }

    public void setTimeToEnd(int timeToEnd) {
        this.timeToEnd = timeToEnd;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayersPerTeam() {
        return minPlayersPerTeam;
    }

    public void setMinPlayersPerTeam(int minPlayersPerTeam) {
        this.minPlayersPerTeam = minPlayersPerTeam;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public void setMaxPlayersPerTeam(int maxPlayersPerTeam) {
        this.maxPlayersPerTeam = maxPlayersPerTeam;
    }

    public List<String> getAllowedMaps() {
        return allowedMaps;
    }

    public String getDefaultMap() {
        return defaultMap;
    }

    public void setDefaultMap(String defaultMap) {
        this.defaultMap = defaultMap;
    }

    public Rules copy() {
        return new Rules(
                timeToStart,
                timeToEnd,
                maxPlayers,
                minPlayersPerTeam,
                maxPlayersPerTeam,
                new ArrayList<>(allowedMaps),
                defaultMap
        );
    }

    public static Rules defaultRules() {
        return new Rules(30, 10, 40, 4, 16, null, null);
    }
}
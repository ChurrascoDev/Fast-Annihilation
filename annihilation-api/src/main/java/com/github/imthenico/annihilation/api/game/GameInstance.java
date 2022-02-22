package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.property.PropertyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface GameInstance extends PropertyHolder {

    GameLobby getLobby();

    State getState();

    String id();

    Rules getRules();

    Options getOptions();

    PreMatchStage getPreparationStage();

    AuthorizationResult startMatch();

    Match getMatch();

    MatchAuthorizer getMatchAuthorizer();

    void join(AnniPlayer anniPlayer);

    void leave(AnniPlayer anniPlayer);

    boolean isInGame(AnniPlayer anniPlayer);

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void discardMatch();

    List<AnniPlayer> getPlayers(Predicate<AnniPlayer> filter);

    class Options {
        private int nexusDamage = 1;
        private boolean nexusInvulnerability;
        private boolean doubleNexusDamage;

        public void setNexusDamage(int nexusDamage) {
            this.nexusDamage = nexusDamage;
        }

        public void setNexusInvulnerability(boolean nexusInvulnerability) {
            this.nexusInvulnerability = nexusInvulnerability;
        }

        public void setDoubleNexusDamage(boolean doubleNexusDamage) {
            this.doubleNexusDamage = doubleNexusDamage;
        }

        public int getNexusDamage() {
            return nexusDamage;
        }

        public boolean areNexusInvulnerable() {
            return nexusInvulnerability;
        }

        public boolean isDoubleNexusDamage() {
            return doubleNexusDamage;
        }
    }

    class Rules {

        private int timeToStart;
        private int timeToEnd;
        private int minPlayers;
        private int maxPlayers;
        private int minPlayersPerTeam;
        private int maxPlayersPerTeam;
        private final List<String> allowedMaps;
        private String defaultMap;

        public Rules(
                int timeToStart,
                int timeToEnd,
                int minPlayers,
                int maxPlayers,
                int minPlayersPerTeam,
                int maxPlayersPerTeam,
                List<String> allowedMaps,
                String defaultMap
        ) {
            this.timeToStart = timeToStart;
            this.timeToEnd = timeToEnd;
            this.minPlayers = minPlayers;
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

        public int getMinPlayers() {
            return minPlayers;
        }

        public void setMinPlayers(int minPlayers) {
            this.minPlayers = minPlayers;
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
    }

    enum State {
        WAITING,
        STARTING,
        IN_GAME,
        ENDING;
    }

    static Rules defaultRules() {
        return new Rules(30, 10, 16, 40, 4, 10, null, null);
    }
}
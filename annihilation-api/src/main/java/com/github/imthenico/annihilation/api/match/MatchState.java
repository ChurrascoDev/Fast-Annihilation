package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MatchState {

    private final List<MatchPlayer> alivePlayers;
    private final List<MatchTeam> aliveTeams;
    private final boolean tie;
    private final Match match;

    public MatchState(
            List<MatchPlayer> alivePlayers,
            List<MatchTeam> aliveTeams,
            boolean tie,
            Match match
    ) {
        this.alivePlayers = Objects.requireNonNull(alivePlayers);
        this.aliveTeams = Objects.requireNonNull(aliveTeams);
        this.tie = tie;
        this.match = match;
    }

    public List<MatchPlayer> getAlivePlayers() {
        return alivePlayers;
    }

    public List<MatchTeam> getAliveTeams() {
        return aliveTeams;
    }

    public boolean tiedMatch() {
        return tie;
    }

    public Match getMatch() {
        return match;
    }

    public Optional<MatchTeam> getWinner() {
        MatchTeam alive = null;

        for (TeamColor value : TeamColor.values()) {
            MatchTeam matchTeam = match.getRunningMap().getTeam(value);

            if (matchTeam.isDeath()) {
                continue;
            }

            // two or more teams alive (tie)
            if (alive != null) {
                return Optional.empty();
            }

            alive = matchTeam;
        }

        return Optional.ofNullable(alive);
    }

    public static MatchState of(Match match) {
        List<MatchPlayer> alivePlayers = new ArrayList<>(match.getPlayers((p) -> !p.isDisqualified()));
        List<MatchTeam> aliveTeams = new ArrayList<>();

        for (TeamColor value : TeamColor.values()) {
            MatchTeam team = match.getRunningMap().getTeam(value);

            if (!team.isDeath())
                aliveTeams.add(team);
        }

        return new MatchState(alivePlayers, aliveTeams, match.allPhasesFinished(), match);
    }
}
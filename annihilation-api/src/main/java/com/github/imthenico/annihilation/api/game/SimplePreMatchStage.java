package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.*;

public class SimplePreMatchStage implements PreMatchStage {

    private final VoteCounter<UUID, ConfigurableModel> votes;
    private final Map<UUID, TeamColor> teamSelection;
    private final SimpleTimer timer;

    public SimplePreMatchStage(
            int timeToStart
    ) {
        this.votes = new VoteCounter<>();
        this.teamSelection = new HashMap<>();
        this.timer = new SimpleTimer(timeToStart);
    }

    @Override
    public VoteCounter<UUID, ConfigurableModel> getVotes() {
        return votes;
    }

    @Override
    public Map<UUID, TeamColor> getTeamSelection() {
        return Collections.unmodifiableMap(teamSelection);
    }

    @Override
    public TeamColor joinTeam(AnniPlayer anniPlayer, TeamColor color) {
        return this.teamSelection.put(anniPlayer.getId(), Validate.notNull(color));
    }

    @Override
    public TeamColor leaveTeam(AnniPlayer anniPlayer) {
        return this.teamSelection.remove(anniPlayer.getId());
    }

    @Override
    public SimpleTimer getCountdownToStart() {
        return timer;
    }
}
package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.annihilation.api.util.VoteCounter;

import java.util.Map;
import java.util.UUID;

public interface PreMatchStage {

    VoteCounter<UUID, ConfigurableModel> getVotes();

    Map<UUID, TeamColor> getTeamSelection();

    TeamColor joinTeam(AnniPlayer anniPlayer, TeamColor color);

    TeamColor leaveTeam(AnniPlayer anniPlayer);

    SimpleTimer getCountdownToStart();

}
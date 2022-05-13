package com.github.imthenico.fastannihilation.game;

import com.github.imthenico.annihilation.api.event.game.PlayerSelectTeamEvent;
import com.github.imthenico.annihilation.api.event.game.PlayerUnselectTeamEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.fastannihilation.map.MapVoteCounter;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimplePreMatchStage implements PreMatchStage {

    private final VoteCounter<UUID, String> votes;
    private final Map<UUID, TeamColor> teamSelection;
    private final SimpleTimer timer;
    private final Game game;

    public SimplePreMatchStage(
            int timeToStart,
            Game game,
            MatchMapModelProvider matchMapModelProvider
    ) {
        this.game = game;
        this.votes = new MapVoteCounter<>(matchMapModelProvider, game);
        this.teamSelection = new HashMap<>();
        this.timer = new SimpleTimer(timeToStart);
    }

    @Override
    public VoteCounter<UUID, String> getVotes() {
        return votes;
    }

    @Override
    public Map<UUID, TeamColor> getTeamSelection() {
        return Collections.unmodifiableMap(teamSelection);
    }

    @Override
    public TeamColor joinTeam(AnniPlayer anniPlayer, TeamColor color) {
        TeamColor prev = this.teamSelection.get(anniPlayer.getId());

        PlayerSelectTeamEvent event = new PlayerSelectTeamEvent(game, anniPlayer, color, prev);

        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return null;

        return this.teamSelection.put(anniPlayer.getId(), color);
    }

    @Override
    public TeamColor leaveTeam(AnniPlayer anniPlayer) {
        TeamColor prev = this.teamSelection.get(anniPlayer.getId());

        PlayerUnselectTeamEvent event = new PlayerUnselectTeamEvent(game, anniPlayer, prev);

        Bukkit.getPluginManager().callEvent(event);

        return this.teamSelection.remove(anniPlayer.getId());
    }

    @Override
    public SimpleTimer getCountdownToStart() {
        return timer;
    }
}
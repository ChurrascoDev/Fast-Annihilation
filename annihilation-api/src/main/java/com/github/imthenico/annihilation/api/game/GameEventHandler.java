package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;

public interface GameEventHandler {

    void handleRoomLeave(AnniPlayer player);

    void handleGameJoin(AnniPlayer player);

    void handleTeamJoin(AnniPlayer player, TeamColor lastTeam, TeamColor newTeam);

    void handleTeamLeave(AnniPlayer player, TeamColor team);

    void handleMapVote(AnniPlayer player, MatchMap lastVoted, ConfigurableModel votedMap);
    
}
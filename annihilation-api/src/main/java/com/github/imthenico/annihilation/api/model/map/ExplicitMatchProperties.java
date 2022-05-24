package com.github.imthenico.annihilation.api.model.map;

import com.github.imthenico.annihilation.api.team.TeamColor;
import org.bukkit.Location;

import java.util.List;

public interface ExplicitMatchProperties {

    Nexus getTeamNexus(TeamColor color);

    Nexus getNexusByLocation(Location location);

    List<Location> getTeamSpawns(TeamColor color);

    List<Location> getSpectatorPositions(TeamColor color);

    List<Location> getFurnaces();

    int getTimePerPhase();

    int getPhaseTime(int phase);

    // special blocks getter

}
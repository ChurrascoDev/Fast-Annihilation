package com.github.imthenico.annihilation.api.model.map;

import com.github.imthenico.annihilation.api.data.DataHolder;
import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.annihilation.api.model.NexusModel;
import com.github.imthenico.annihilation.api.model.TeamDataModel;
import com.github.imthenico.annihilation.api.team.DefaultTeam;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SafeCast;
import com.github.imthenico.gmlib.GameMap;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldContainer;
import com.github.imthenico.inject.annotation.InjectAll;
import com.github.imthenico.inject.annotation.Skip;
import org.bukkit.Location;

import java.util.*;

public class MatchMap extends GameMap implements DataHolder, ExplicitMatchProperties {

    private final String name;
    private final Map<TeamColor, MatchTeam> teamMap;
    private final Map<String, Object> data;

    private final Map<TeamColor, Nexus> nexusMap;
    private final Map<TeamColor, List<Location>> teamSpawns;
    private final Map<TeamColor, List<Location>> spectatorPositions;
    private final int timePerPhase;
    private final Map<Integer, Integer> phasesDuration;
    private final List<Location> furnaces;

    @InjectAll(
            from = MatchMapData.class,
            valuesToExtract = {}
    )
    public MatchMap(
            @Skip MatchMapData matchMapData,
            @Skip AWorld mainWorld,
            @Skip WorldContainer<AWorld> worldContainer,
            @Skip String name
    ) {
        super(matchMapData, mainWorld, worldContainer, name);

        this.name = name;
        this.teamMap = new HashMap<>();

        for (TeamColor value : TeamColor.values()) {
            MatchTeam matchTeam = new DefaultTeam(value, this);

            teamMap.put(value, matchTeam);
        }

        this.data = new HashMap<>();
        this.nexusMap = new HashMap<>();
        this.teamSpawns = new HashMap<>();
        this.spectatorPositions = new HashMap<>();
        this.timePerPhase = matchMapData.getTimePerPhase();
        this.phasesDuration = matchMapData.getPhasesDuration();
        WorldContainer<AWorld> allWorlds = allWorlds();

        this.furnaces = transformLocations(matchMapData.getFurnaces(), allWorlds);

        matchMapData.getTeamData().forEach((color, teamDataModel) -> {
            nexusMap.put(color, getNexus(color, teamDataModel, allWorlds));

            teamSpawns.put(color, transformLocations(teamDataModel.getSpawns(), allWorlds));
            spectatorPositions.put(color, transformLocations(teamDataModel.getSpectatorPositions(), allWorlds));
        });
    }

    public String getName() {
        return name;
    }

    @Override
    public Nexus getTeamNexus(TeamColor color) {
        return nexusMap.get(color);
    }

    @Override
    public List<Location> getTeamSpawns(TeamColor color) {
        return teamSpawns.get(color);
    }

    @Override
    public List<Location> getSpectatorPositions(TeamColor color) {
        return spectatorPositions.get(color);
    }

    @Override
    public List<Location> getFurnaces() {
        return furnaces;
    }

    @Override
    public int getTimePerPhase() {
        return timePerPhase;
    }

    @Override
    public int getPhaseTime(int phase) {
        return phasesDuration.getOrDefault(phase, 0);
    }

    @Override
    public Object putValue(String key, Object value) {
        return data.put(key, value);
    }

    @Override
    public Object removeValue(String key) {
        return data.remove(key);
    }

    @Override
    public SafeCast<?> getValue(String key) {
        return SafeCast.of(data.get(key));
    }

    @Override
    public boolean has(String key) {
        return data.containsKey(key);
    }

    public MatchTeam getTeam(TeamColor color) {
        return teamMap.get(color);
    }

    private static List<Location> transformLocations(List<LocationModel> locationModels, WorldContainer<AWorld> worldContainer) {
        List<Location> locations = new ArrayList<>(locationModels.size());

        for (LocationModel locationModel : locationModels) {
            locations.add(locationModel.toBukkit(worldContainer));
        }

        return locations;
    }

    private Nexus getNexus(TeamColor color, TeamDataModel teamDataModel, WorldContainer<AWorld> worldContainer) {
        NexusModel nexusModel = teamDataModel.nexus();

        if (nexusModel == null)
            throw new IllegalArgumentException("Nexus in '" + color + "' data is null");

        return new SimpleNexus(
                nexusModel.totalHealth,
                color,
                nexusModel.getLocation().toBukkit(worldContainer),
                this
        );
    }
}
package com.github.imthenico.annihilation.api.model.map;

import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.annihilation.api.model.TeamDataModel;
import com.github.imthenico.annihilation.api.model.EditableMapData;
import com.github.imthenico.annihilation.api.team.TeamColor;

import java.util.*;

public class MatchMapData extends EditableMapData {

    private final Map<TeamColor, TeamDataModel> teamData;
    private final Map<Integer, Integer> phasesDuration;
    private final List<LocationModel> furnaces;
    private int timePerPhase;

    public MatchMapData(
            Map<TeamColor, TeamDataModel> teamData,
            Map<Integer, Integer> phasesDuration,
            List<LocationModel> furnaces
    ) {
        this.teamData = teamData;
        this.phasesDuration = phasesDuration;
        this.furnaces = furnaces;
    }

    public MatchMapData() {
        this(new HashMap<>(), new HashMap<>(), new LinkedList<>());
    }

    @Override
    public boolean editable() {
        return true;
    }

    @Override
    public void accept(EditableMapData modelData) throws UnsupportedOperationException {
        if (!(modelData instanceof MatchMapData))
            throw new IllegalArgumentException("Invalid model data");

        teamData.clear();
        phasesDuration.clear();
        furnaces.clear();

        MatchMapData matchMapModelData = (MatchMapData) modelData.copy();

        this.teamData.putAll(matchMapModelData.teamData);
        this.phasesDuration.putAll(matchMapModelData.phasesDuration);
        this.furnaces.addAll(matchMapModelData.furnaces);
        this.timePerPhase = matchMapModelData.timePerPhase;
    }

    public Map<TeamColor, TeamDataModel> getTeamData() {
        return teamData;
    }

    public List<LocationModel> getFurnaces() {
        return furnaces;
    }

    public Map<Integer, Integer> getPhasesDuration() {
        return phasesDuration;
    }

    public int getTimePerPhase() {
        return timePerPhase;
    }

    public void setTeamData(TeamColor color, TeamDataModel teamDataModel) {
        this.teamData.put(Objects.requireNonNull(color), teamDataModel.copy());
    }

    public void addFurnace(LocationModel model) {
        this.furnaces.add(model.copy());
    }

    public void setTimePerPhase(int timePerPhase) {
        this.timePerPhase = timePerPhase > 0 ? timePerPhase : 300;
    }

    public TeamDataModel getTeamData(TeamColor color) {
        return teamData.computeIfAbsent(color, (k) -> new TeamDataModel());
    }

    @Override
    public MatchMapData copy() {
        MatchMapData matchMapModelData = new MatchMapData();

        this.teamData.forEach(matchMapModelData::setTeamData);
        this.furnaces.forEach(matchMapModelData::addFurnace);
        matchMapModelData.setTimePerPhase(timePerPhase);
        this.getExtraData().entrySet().forEach(entry -> matchMapModelData.getExtraData().add(entry.getKey(), entry.getValue()));

        return matchMapModelData;
    }
}
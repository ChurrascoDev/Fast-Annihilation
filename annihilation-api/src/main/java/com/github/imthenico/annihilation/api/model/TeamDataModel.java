package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.json.JsonSerializable;
import com.github.imthenico.json.JsonTreeBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TeamDataModel implements JsonSerializable {

    private final List<LocationModel> spawns;
    private final List<LocationModel> spectatorPositions;
    private NexusModel nexus;

    public TeamDataModel() {
        this(new ArrayList<>(), new ArrayList<>(), null);
    }

    public TeamDataModel(
            List<LocationModel> spawns,
            List<LocationModel> spectatorPositions,
            NexusModel nexus
    ) {
        this.spawns = spawns;
        this.spectatorPositions = spectatorPositions;
        this.nexus = nexus;
    }

    public void setNexus(NexusModel nexus) {
        this.nexus = nexus.copy();
    }

    public @Nullable NexusModel nexus() {
        return nexus;
    }

    public void addSpawn(LocationModel model) {
        spawns.add(Objects.requireNonNull(model));
    }

    public void addSpectatorPos(LocationModel model) {
        spectatorPositions.add(Objects.requireNonNull(model));
    }

    public void removeSpawn(LocationModel model) {
        spawns.remove(model);
    }

    public void removeSpectatorPos(LocationModel model) {
        spawns.remove(model);
    }

    public List<LocationModel> getSpawns() {
        return Collections.unmodifiableList(spawns);
    }

    public List<LocationModel> getSpectatorPositions() {
        return Collections.unmodifiableList(spectatorPositions);
    }

    public TeamDataModel copy() {
        TeamDataModel teamDataModel = new TeamDataModel();

        spawns.forEach(teamDataModel::addSpawn);
        spectatorPositions.forEach(teamDataModel::addSpectatorPos);

        if (nexus != null) {
            teamDataModel.setNexus(nexus);
        }

        return teamDataModel;
    }

    @Override
    public @NotNull JsonElement serialize() {
        return JsonTreeBuilder.create()
                .addAll("spawns", spawns)
                .addAll("spectatorPositions", spectatorPositions)
                .add("nexus", nexus)
                .build();
    }
}
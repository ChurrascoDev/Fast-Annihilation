package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.game.Rules;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.TeamDataModel;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.ModelData;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public interface GameValidation {

    static boolean hasAvailableMaps(Game game, ModelCache modelService) {
        Rules rules = game.getRules();
        List<String> allowedMapNames = rules.getAllowedMaps();

        if (allowedMapNames.isEmpty()) {
            return modelService.count() > 0;
        }

        for (MapModel<?> value : modelService) {
            if (!allowedMapNames.contains(value.getName()))
                continue;

            ModelData mapData = value.getData();

            if (mapData instanceof MatchMapData)
                return true;
        }

        return false;
    }

    static boolean balancedTeams(Game game) {
        PreMatchStage preparationStage = game.getPreparationStage();

        Map<UUID, TeamColor> playersTeamSelection = preparationStage.getTeamSelection();
        Map<TeamColor, AtomicInteger> teamSizeMap = new HashMap<>();

        for (Map.Entry<UUID, TeamColor> entry : playersTeamSelection.entrySet()) {
            teamSizeMap.computeIfAbsent(entry.getValue(), (k) -> new AtomicInteger()).incrementAndGet();
        }

        for (TeamColor value : TeamColor.values()) {
            AtomicInteger atomicInteger = teamSizeMap.get(value);

            if (atomicInteger == null || atomicInteger.get() < game.getRules().getMinPlayersPerTeam())
                return false;
        }

        return true;
    }

    static boolean isDataCorrectlyConfigured(MatchMapData data) {
        for (TeamColor value : TeamColor.values()) {
            TeamDataModel teamDataModel = data.getTeamData().get(value);

            if (
                    teamDataModel.getSpawns().isEmpty()
                    || teamDataModel.nexus() == null
                    || teamDataModel.getSpectatorPositions().isEmpty()
            ) {
                return false;
            }
        }

        return true;
    }
}
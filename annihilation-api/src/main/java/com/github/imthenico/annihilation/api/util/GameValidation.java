package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.map.ConfigurableModelManager;
import com.github.imthenico.annihilation.api.map.model.NexusModel;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.property.PropertyKeys;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.minecraft.LocationModel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public interface GameValidation {

    static boolean hasAvailableMaps(GameInstance game, ConfigurableModelManager configurableModelManager) {
        GameInstance.Rules rules = game.getRules();
        List<String> allowedMapNames = rules.getAllowedMaps();
        Set<ConfigurableModel> availableMaps = new HashSet<>();

        if (allowedMapNames.isEmpty()) {
            configurableModelManager.getModels().forEach((k, v) -> availableMaps.add(v));
        } else {
            for (String allowedMapName : allowedMapNames) {
                ConfigurableModel configurableModel = configurableModelManager.getModel(allowedMapName);

                if (configurableModel == null || !ModelUtil.hasTag(configurableModel, "map-model"))
                    continue;

                availableMaps.add(configurableModel);
            }
        }

        return !availableMaps.isEmpty();
    }

    static boolean balancedTeams(GameInstance game) {
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

    static boolean isMapCorrectlyConfigured(ConfigurableModel mapModel) {
        if (!ModelUtil.hasTag(mapModel, "map-model")) {
            return false;
        }

        PropertiesContainer properties = mapModel.getProperties();

        for (TeamColor value : TeamColor.values()) {
            Collection<LocationModel> spawns = properties.getProperty(PropertyKeys.teamSpawns(value))
                    .orDefault(Collections::emptyList);

            NexusModel nexus = properties.getProperty(PropertyKeys.teamNexus(value))
                    .orDefault(() -> null);

            Collection<LocationModel> spectatorSpawns = properties.getProperty(PropertyKeys.teamSpectatorPositions(value))
                    .orDefault(Collections::emptyList);

            if (
                    spawns.isEmpty()
                    || nexus == null
                    || spectatorSpawns.isEmpty()
            ) {
                return false;
            }
        }

        return true;
    }
}
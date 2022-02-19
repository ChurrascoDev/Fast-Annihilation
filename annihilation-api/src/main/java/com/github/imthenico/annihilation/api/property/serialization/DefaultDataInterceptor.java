package com.github.imthenico.annihilation.api.property.serialization;

import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.data.exception.MappingException;
import com.github.imthenico.simplecommons.data.node.AdapterNode;
import com.github.imthenico.simplecommons.minecraft.LocationModel;

import java.util.List;

import static com.github.imthenico.annihilation.api.property.PropertyKeys.*;

public class DefaultDataInterceptor implements DataInterceptor {

    @Override
    public void intercept(PropertiesContainer propertiesContainer, AdapterNode treeNode) {
        set(timePerPhase(), propertiesContainer, treeNode);

        try {
            // furnaces
            PropertyKey furnacesKey = furnaces();
            List<LocationModel> locations = treeNode.prepareForMap(furnacesKey.getName()).mapAll(LocationModel.class);

            propertiesContainer.set(furnacesKey, locations);

            for (TeamColor value : TeamColor.values()) {
                // team spawns
                PropertyKey teamSpawnsKey = teamSpawns(value);

                List<LocationModel> teamSpawns = treeNode.prepareForMap(teamSpawnsKey.getName()).mapAll(LocationModel.class);
                propertiesContainer.set(teamSpawnsKey, teamSpawns);

                // team spectator positions
                PropertyKey spectatorPositionsKey = teamSpectatorPositions(value);

                List<LocationModel> teamPositions = treeNode.prepareForMap(spectatorPositionsKey.getName()).mapAll(LocationModel.class);
                propertiesContainer.set(spectatorPositionsKey, teamPositions);

                // team nexus
                PropertyKey teamNexusKey = teamNexus(value);

                List<LocationModel> teamNexus = treeNode.prepareForMap(teamNexusKey.getName()).mapAll(LocationModel.class);
                propertiesContainer.set(teamNexusKey, teamNexus);
            }
        } catch (MappingException e) {
            e.printStackTrace();
        }
    }
}
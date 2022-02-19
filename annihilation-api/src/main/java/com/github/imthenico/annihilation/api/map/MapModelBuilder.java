package com.github.imthenico.annihilation.api.map;

import com.github.imthenico.annihilation.api.map.model.NexusModel;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.model.SimpleConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.property.SimplePropertiesContainer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.TypeTokens;
import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;
import com.github.imthenico.simplecommons.minecraft.LocationModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.imthenico.annihilation.api.property.PropertyKeys.*;
import static com.github.imthenico.annihilation.api.util.MapPropertiesHelper.*;

public interface MapModelBuilder {

    MapModelBuilder setWorld(LoadedWorldTemplate world);

    MapModelBuilder addComplementaryWorld(LoadedWorldTemplate complementaryWorld);

    MapModelBuilder addTeamSpawn(TeamColor color, LocationModel locationModel);

    MapModelBuilder addTeamSpectatorSpawn(TeamColor color, LocationModel locationModel);

    MapModelBuilder addTeamNexus(TeamColor color, NexusModel locationModel);

    MapModelBuilder addFurnace(LocationModel locationModel);

    MapModelBuilder addProperty(PropertyKey key, Object property);

    ConfigurableModel build();

    static MapModelBuilder newBuilder(String name) {
        return new Impl(name);
    }

    class Impl implements MapModelBuilder {

        private final String name;

        private LoadedWorldTemplate world;
        private final PropertiesContainer properties = new SimplePropertiesContainer();
        private final Map<String, LoadedWorldTemplate> complementaryWorlds = new ConcurrentHashMap<>();

        public Impl(String name) {
            this.name = Validate.notNull(name);
        }

        @Override
        public MapModelBuilder setWorld(LoadedWorldTemplate world) {
            this.world = Validate.notNull(world);
            return this;
        }

        @Override
        public MapModelBuilder addComplementaryWorld(LoadedWorldTemplate complementaryWorld) {
            this.complementaryWorlds.put(complementaryWorld.getSource().getName(), complementaryWorld);
            return this;
        }

        @Override
        public MapModelBuilder addTeamSpawn(TeamColor color, LocationModel locationModel) {
            properties.compute(teamSpawns(color), ArrayList::new)
                    .get(TypeTokens.list(LocationModel.class))
                    .add(Validate.notNull(locationModel));

            return this;
        }

        @Override
        public MapModelBuilder addTeamSpectatorSpawn(TeamColor color, LocationModel locationModel) {
            properties.compute(teamSpectatorPositions(color), ArrayList::new)
                    .get(TypeTokens.list(LocationModel.class))
                    .add(Validate.notNull(locationModel));

            return this;
        }

        @Override
        public MapModelBuilder addTeamNexus(TeamColor color, NexusModel nexusModel) {
            setNexusModel(color, Validate.notNull(nexusModel), properties);
            return this;
        }

        @Override
        public MapModelBuilder addFurnace(LocationModel locationModel) {
            properties.compute(furnaces(), ArrayList::new)
                    .get(TypeTokens.list(LocationModel.class))
                    .add(Validate.notNull(locationModel));

            return this;
        }

        @Override
        public MapModelBuilder addProperty(PropertyKey key, Object property) {
            properties.set(key, property);
            return this;
        }

        @Override
        public ConfigurableModel build() {
            return new SimpleConfigurableModel(
                    name,
                    world,
                    complementaryWorlds,
                    properties
            );
        }
    }
}
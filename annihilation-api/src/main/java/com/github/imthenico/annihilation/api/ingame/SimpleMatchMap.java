package com.github.imthenico.annihilation.api.ingame;

import com.github.imthenico.annihilation.api.entity.Entity;
import com.github.imthenico.annihilation.api.entity.EntityId;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.model.SimpleLoadedModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.team.DefaultTeam;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SafeCast;
import com.github.imthenico.annihilation.api.world.SimpleWorld;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleMatchMap extends SimpleLoadedModel implements MatchMap {

    private final String name;
    private final Map<TeamColor, MatchTeam> teamMap;
    private final Map<EntityId, Entity<?>> entityMap;
    private final PropertiesContainer propertiesContainer;

    public SimpleMatchMap(
            String name,
            SimpleWorld mainWorld,
            Map<String, SimpleWorld> worlds,
            ConfigurableModel mapModel,
            PropertiesContainer propertiesContainer
    ) {
        super(mainWorld, worlds, mapModel, propertiesContainer);

        this.name = name;

        this.teamMap = new HashMap<>(4);

        for (TeamColor value : TeamColor.values()) {
            teamMap.put(value, new DefaultTeam(value, this));
        }

        this.entityMap = new HashMap<>();
        this.propertiesContainer = propertiesContainer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MatchTeam getTeam(TeamColor color) {
        return teamMap.get(color);
    }

    @Override
    public SafeCast<Entity<?>> getEntity(EntityId entityId) {
        Entity<?> entity = entityMap.get(entityId);

        return SafeCast.of(entity);
    }

    @Override
    public void addEntity(EntityId entityId, Entity<?> entity) {
        Entity<?> lastEntity = entityMap.put(entityId, entity);

        if (lastEntity != null)
            lastEntity.remove();
    }

    @Override
    public void removeEntity(EntityId entityId) {
        Entity<?> lastEntity = entityMap.remove(entityId);

        if (lastEntity != null)
            lastEntity.remove();
    }

    @Override
    public Set<EntityId> getEntitiesId() {
        return Collections.unmodifiableSet(entityMap.keySet());
    }

    @Override
    public PropertiesContainer getProperties() {
        return propertiesContainer;
    }
}
package com.github.imthenico.annihilation.api.map;

import com.github.imthenico.annihilation.api.entity.Entity;
import com.github.imthenico.annihilation.api.entity.EntityId;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.model.LoadedModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyHolder;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SafeCast;
import com.github.imthenico.annihilation.api.world.SimpleWorld;
import com.github.imthenico.simplecommons.util.list.ImmutableCustomList;

import java.util.Map;
import java.util.Set;

public interface MapContext extends LoadedModel {

    MatchTeam getTeam(TeamColor color);

    SafeCast<Entity<?>> getEntity(EntityId entityId);

    void addEntity(EntityId entityId, Entity<?> entity);

    void removeEntity(EntityId entityId);

    Set<EntityId> getEntitiesId();

    @Override
    PropertiesContainer getProperties();
}
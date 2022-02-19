package com.github.imthenico.annihilation.api.converter;

import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.ingame.SimpleMatchMap;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.world.SimpleWorld;

import java.util.Map;

public class ConverterToMatchMap extends AbstractConverter<MatchMap> {

    @Override
    protected String[] getRequiredTags() {
        return new String[] {"map-model"};
    }

    @Override
    protected MatchMap create(
            ConfigurableModel model,
            Map<String, SimpleWorld> worlds,
            PropertiesContainer propertiesContainer,
            Map<String, Object> extraData
    ) {
        return new SimpleMatchMap(
                model.getId(),
                worlds.get("main"),
                worlds,
                model,
                getProperties(model.getProperties())
        );
    }
}
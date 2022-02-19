package com.github.imthenico.annihilation.api.converter;

import com.github.imthenico.annihilation.api.exception.ConverterParseException;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyInterpreter;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.property.SimplePropertiesContainer;
import com.github.imthenico.annihilation.api.util.ModelUtil;
import com.github.imthenico.annihilation.api.util.TemporaryWorldGenerator;
import com.github.imthenico.annihilation.api.world.SimpleWorld;
import com.github.imthenico.annihilation.api.world.WorldTemplate;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.*;

public abstract class AbstractConverter<T> implements ModelConverter<T> {

    protected final Map<Class<?>, PropertyInterpreter<?, ?>> interpreterMap;
    private final TemporaryWorldGenerator worldGenerator;

    public AbstractConverter() {
        this.interpreterMap = new HashMap<>();
        this.worldGenerator = TemporaryWorldGenerator.getInstance();
    }

    @Override
    public T convert(ConfigurableModel model, Map<String, Object> extraData) throws ConverterParseException {
        String[] tags = getRequiredTags();

        if (ModelUtil.hasTag(model, tags))
            throw new ConverterParseException("Model is not tagged as: " + Arrays.toString(tags));

        Map<String, SimpleWorld> worlds = generateWorlds(model);

        return create(model, worlds, getProperties(model.getProperties()), Validate.defIfNull(extraData, Collections.emptyMap()));
    }

    protected abstract String[] getRequiredTags();

    protected abstract T create(
            ConfigurableModel model,
            Map<String, SimpleWorld> worlds,
            PropertiesContainer propertiesContainer,
            Map<String, Object> extraData
    );

    @Override
    public <S> ModelConverter<T> addMapPropertyInterpreter(Class<S> propertyValueType, PropertyInterpreter<S, ?> interpreter) {
        Validate.notNull(propertyValueType);

        if (interpreter == null) {
            this.interpreterMap.remove(propertyValueType);
            return this;
        }

        this.interpreterMap.put(propertyValueType, interpreter);

        return this;
    }

    protected PropertiesContainer getProperties(PropertiesContainer propertiesContainer) {
        PropertiesContainer properties = new SimplePropertiesContainer();

        for (PropertyKey key : propertiesContainer.keys()) {
            Object propertyOrProperties = propertiesContainer.getProperty(key).get();

            if (propertyOrProperties instanceof Collection) {
                Collection<?> propertyList = (Collection<?>) propertyOrProperties;
                List<Object> list = new ArrayList<>(propertyList.size());

                for (Object property : propertyList) {
                    list.add(interpret(key, property));
                }

                properties.set(key, list);
            } else {
                properties.set(key, interpret(key, propertyOrProperties));
            }
        }

        return properties;
    }

    @SuppressWarnings("unchecked")
    protected Object interpret(PropertyKey key, Object property) {
        PropertyInterpreter<Object, ?> interpreter = (PropertyInterpreter<Object, ?>) interpreterMap.get(property.getClass());

        Object interpreted = interpreter.readProperty(key, property);
        return Validate.defIfNull(interpreted, property);
    }

    protected Map<String, SimpleWorld> generateWorlds(ConfigurableModel mapModel) {
        Map<String, SimpleWorld> worlds = new HashMap<>();
        WorldTemplate worldTemplate = mapModel.getMainWorld();

        worlds.put("main", worldGenerator.generate(worldTemplate));

        for (WorldTemplate value : mapModel.getWorlds().values()) {
            worlds.put(value.getSource().getName(), worldGenerator.generate(value));
        }

        return worlds;
    }
}
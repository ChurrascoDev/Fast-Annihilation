package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.util.SafeCast;

import java.util.*;
import java.util.function.Supplier;

public class SimplePropertiesContainer implements PropertiesContainer {

    private final Map<PropertyKey, Object> propertyMap;

    public SimplePropertiesContainer() {
        this.propertyMap = new HashMap<>();
    }

    @Override
    public SafeCast<?> getProperty(PropertyKey key) {
        return SafeCast.of(propertyMap.get(key));
    }

    @Override
    public String getString(PropertyKey key, String def) {
        return getProperty(key).orDefault(() -> def);
    }

    @Override
    public String getString(PropertyKey key) {
        return Objects.toString(getProperty(key).get(), null);
    }

    @Override
    public boolean getBoolean(PropertyKey key) {
        Object found = getProperty(key).get();

        if (found == null)
            return false;

        if (found instanceof Boolean)
            return (Boolean) found;

        return Boolean.parseBoolean(found.toString());
    }

    @Override
    public int getInt(PropertyKey key) {
        return getAsNumber(key).intValue();
    }

    @Override
    public double getDouble(PropertyKey key) {
        return getAsNumber(key).doubleValue();
    }

    @Override
    public float getFloat(PropertyKey key) {
        return getAsNumber(key).floatValue();
    }

    @Override
    public long getLong(PropertyKey key) {
        return getAsNumber(key).longValue();
    }

    @Override
    public byte getByte(PropertyKey key) {
        return getAsNumber(key).byteValue();
    }

    @Override
    public <E> List<E> getList(PropertyKey key) {
        return getProperty(key).orDefault(Collections::emptyList);
    }

    @Override
    public Number getAsNumber(PropertyKey key, Number def) {
        return getProperty(key).orDefault(() -> def);
    }

    @Override
    public Number getAsNumber(PropertyKey key) {
        return getAsNumber(key, 0);
    }

    @Override
    public boolean set(PropertyKey key, Object value) {
        if (!canOverWrite(key))
            return false;

        propertyMap.put(key, value);
        return false;
    }

    @Override
    public SafeCast<?> compute(PropertyKey key, Supplier<?> supplier) {
        if (!canOverWrite(key))
            return SafeCast.of(null);

        Object found = propertyMap.get(key);

        if (found == null) {
            Object def = supplier.get();

            propertyMap.put(key, def);
            return SafeCast.of(def);
        }

        return SafeCast.of(found);
    }

    @Override
    public boolean remove(PropertyKey key) {
        if (!canOverWrite(key))
            return false;

        propertyMap.remove(key);
        return true;
    }

    @Override
    public Set<PropertyKey> keys() {
        return Collections.unmodifiableSet(propertyMap.keySet());
    }

    @Override
    public int size() {
        return propertyMap.size();
    }

    @Override
    public boolean isEmpty() {
        return propertyMap.isEmpty();
    }

    @Override
    public boolean has(PropertyKey key) {
        return propertyMap.containsKey(key);
    }

    @Override
    public PropertiesContainer copy() {
        SimplePropertiesContainer mapPropertiesContainer = new SimplePropertiesContainer();

        mapPropertiesContainer.propertyMap.putAll(propertyMap);

        return mapPropertiesContainer;
    }

    @Override
    public Map<PropertyKey, Object> apply(PropertiesContainer propertiesContainer) {
        Map<PropertyKey, Object> discarded = new HashMap<>();

        SimplePropertiesContainer another = (SimplePropertiesContainer) propertiesContainer;

        for (Map.Entry<PropertyKey, Object> entry : another.propertyMap.entrySet()) {
            PropertyKey key = entry.getKey();
            Object value = entry.getValue();

            if (!canOverWrite(key)) {
                discarded.put(key, value);
                continue;
            }

            propertyMap.put(key, value);
        }

        return discarded;
    }

    private PropertyKey getStoredKey(PropertyKey key) {
        for (PropertyKey propertyKey : propertyMap.keySet()) {
            if (propertyKey.equals(key)) {
                return propertyKey;
            }
        }

        return null;
    }

    private boolean canOverWrite(PropertyKey key) {
        PropertyKey lastKey = getStoredKey(key);

        return lastKey == null || (lastKey.equals(key) || lastKey.isOverWritable());
    }
}
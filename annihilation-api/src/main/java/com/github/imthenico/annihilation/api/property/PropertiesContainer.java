package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.util.SafeCast;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface PropertiesContainer {

    SafeCast<?> getProperty(PropertyKey key);

    String getString(PropertyKey key, String def);

    String getString(PropertyKey key);

    boolean getBoolean(PropertyKey key);

    int getInt(PropertyKey key);

    double getDouble(PropertyKey key);

    float getFloat(PropertyKey key);

    long getLong(PropertyKey key);

    byte getByte(PropertyKey key);

    <E> List<E> getList(PropertyKey key);

    Number getAsNumber(PropertyKey key, Number def);

    Number getAsNumber(PropertyKey key);

    boolean set(PropertyKey key, Object value);

    SafeCast<?> compute(PropertyKey key, Supplier<?> supplier);

    boolean remove(PropertyKey key);

    Set<PropertyKey> keys();

    int size();

    boolean isEmpty();

    boolean has(PropertyKey key);

    PropertiesContainer copy();

    Map<PropertyKey, Object> apply(PropertiesContainer propertiesContainer);

}
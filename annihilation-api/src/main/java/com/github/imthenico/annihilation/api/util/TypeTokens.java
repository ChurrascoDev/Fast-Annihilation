package com.github.imthenico.annihilation.api.util;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface TypeTokens {

    static Type list(Type elementType) {
        return TypeToken.getParameterized(List.class, elementType).getType();
    }

    static Type map(Type keyType, Type valueType) {
        return TypeToken.getParameterized(Map.class, keyType, valueType).getType();
    }

    static Type map(Type type) {
        return TypeToken.getParameterized(Map.class, type, type).getType();
    }

    static TypeToken<?> of(Type type) {
        return TypeToken.get(type);
    }
}
package com.github.imthenico.annihilation.api.util;

import com.google.gson.reflect.TypeToken;

import java.util.List;

public interface TypeTokens {

    static <E> TypeToken<List<E>> list(Class<E> elementType) {
        return new TypeToken<List<E>>() {};
    }
}
package com.github.imthenico.annihilation.api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Registry<T> {

    private final static Map<Class<?>, Registry<?>> registries = new HashMap<>();

    private final Map<String, T> data;

    public Registry() {
        this.data = new HashMap<>();
    }

    public T getValue(String name) {
        return data.get(name);
    }

    public T getOrPut(String name, Supplier<T> def) {
        return data.computeIfAbsent(name, (k) -> def.get());
    }

    public T register(String name, T obj) {
        if (data.containsKey(name))
            throw new IllegalArgumentException(name + " is already bound to a value");

        this.data.put(name, obj);
        return obj;
    }

    public T remove(String name) {
        return data.remove(name);
    }

    public boolean hasKey(String name) {
        return data.containsKey(name);
    }

    public boolean existValue(T value) {
        return data.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> Registry<T> getRegistry(Class<T> clazz) {
        return (Registry<T>) registries.computeIfAbsent(clazz, (k) -> new Registry<T>());
    }
}
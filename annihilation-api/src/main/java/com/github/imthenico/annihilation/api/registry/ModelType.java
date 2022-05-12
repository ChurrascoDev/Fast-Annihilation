package com.github.imthenico.annihilation.api.registry;

import com.github.imthenico.gmlib.ModelData;

import java.util.Objects;
import java.util.function.Supplier;

public class ModelType<T extends ModelData> {

    private final Class<T> clazz;
    private final String description;
    private final Supplier<T> supplier;

    ModelType(Class<T> clazz, String description, Supplier<T> supplier) {
        this.clazz = clazz;
        this.description = description;
        this.supplier = supplier;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public String getDescription() {
        return description;
    }

    public T createNewData() {
        return Objects.requireNonNull(supplier.get(), "Data supplier cannot returns null");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModelType)) return false;
        ModelType<?> modelType = (ModelType<?>) o;
        return clazz.equals(modelType.clazz);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz);
    }

    public static <T extends ModelData> ModelType<T> of(
            Class<T> clazz, String description, Supplier<T> supplier
    ) {
        return new ModelType<>(Objects.requireNonNull(clazz), Objects.requireNonNull(description), Objects.requireNonNull(supplier));
    }
}
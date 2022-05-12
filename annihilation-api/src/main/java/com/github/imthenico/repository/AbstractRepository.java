package com.github.imthenico.repository;

import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public abstract class AbstractRepository<T> extends AsyncRepository<T> {

    protected final Gson gson;
    protected final Class<T> modelClass;

    protected AbstractRepository(
            Executor executor,
            Gson gson,
            Class<T> modelClass
    ) {
        super(executor);
        this.gson = gson;
        this.modelClass = modelClass;
    }

    @Override
    public @NotNull T findAndMap(@NotNull String name) throws NoModelSuchException, InvalidContentException {
        return findAndMap(name, modelClass);
    }

    @Override
    public <S extends T> @NotNull S findAndMap(@NotNull String name, @NotNull Class<S> clazz) throws NoModelSuchException, InvalidContentException {
        JsonObject jsonValue = find(name);

        if (!jsonValue.isJsonObject()) {
            throw new InvalidContentException("Invalid json (is not in a tree format)");
        }

        return gson.fromJson(jsonValue, clazz);
    }

    @Override
    public @NotNull <S extends T> Set<S> findAllAndMap(@NotNull Class<S> clazz) throws InvalidContentException {
        Set<S> models = new HashSet<>();

        for (JsonObject value : findAll()) {
            if (!value.isJsonObject()) {
                throw new InvalidContentException("Invalid json (is not in a tree format)");
            }

            S mapped = gson.fromJson(value, clazz);

            models.add(mapped);
        }

        return models;
    }

    @Override
    public @NotNull Set<T> findAllAndMap() throws InvalidContentException {
        return findAllAndMap(modelClass);
    }

    @Override
    public @NotNull SerializedModel save(@NotNull String name, @NotNull T model) {
        JsonObject jsonObject = gson.toJsonTree(model)
                .getAsJsonObject();

        String serializedJson = gson.toJson(jsonObject);

        SerializedModel serializedModel = new SerializedModel(jsonObject, serializedJson);

        saveData(name, serializedModel);

        return serializedModel;
    }

    protected abstract void saveData(String name, SerializedModel serializedModel);
}
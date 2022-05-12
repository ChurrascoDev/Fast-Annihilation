package com.github.imthenico.repository;

import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Repository<T> {

    @NotNull JsonObject find(@NotNull String name) throws NoModelSuchException, InvalidContentException;

    @NotNull T findAndMap(@NotNull String name) throws NoModelSuchException, InvalidContentException;

    <S extends T> @NotNull S findAndMap(@NotNull String name, @NotNull Class<S> clazz)
            throws NoModelSuchException, InvalidContentException;

    @NotNull Set<JsonObject> findAll() throws InvalidContentException;

    @NotNull Set<T> findAllAndMap() throws InvalidContentException;

    @NotNull <S extends T> Set<S> findAllAndMap(@NotNull Class<S> clazz) throws InvalidContentException;

    @NotNull SerializedModel save(@NotNull String name, @NotNull T model);

    boolean remove(@NotNull String name);

    @NotNull Set<String> keys();

}
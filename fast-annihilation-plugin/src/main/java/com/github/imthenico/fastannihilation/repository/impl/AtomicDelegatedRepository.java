package com.github.imthenico.fastannihilation.repository.impl;

import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.repository.SerializedModel;
import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicDelegatedRepository<T> extends AbstractRepository<T> {

    private final AtomicReference<AbstractRepository<T>> delegate;

    public AtomicDelegatedRepository(
            AtomicReference<AbstractRepository<T>> delegate
    ) {
        super(null, null, null);
        this.delegate = delegate;
    }


    private AbstractRepository<T> getRepository() {
        return delegate.get();
    }

    @Override
    protected void saveData(String name, SerializedModel serializedModel) {}

    @Override
    public @NotNull JsonObject find(@NotNull String name)
            throws NoModelSuchException, InvalidContentException {
        return getRepository().find(name);
    }

    @Override
    public @NotNull Set<JsonObject> findAll() throws InvalidContentException {
        return getRepository().findAll();
    }

    @Override
    public boolean remove(@NotNull String name) {
        return getRepository().remove(name);
    }

    @Override
    public @NotNull Set<String> keys() {
        return getRepository().keys();
    }

    @Override
    @NotNull
    public T findAndMap(@NotNull String name) throws NoModelSuchException, InvalidContentException {
        return getRepository().findAndMap(name);
    }

    @Override
    public <S extends T> @NotNull S findAndMap(@NotNull String name, @NotNull Class<S> clazz) throws NoModelSuchException, InvalidContentException {
        return getRepository().findAndMap(name, clazz);
    }

    @Override
    @NotNull
    public <S extends T> Set<S> findAllAndMap(@NotNull Class<S> clazz) throws InvalidContentException {
        return getRepository().findAllAndMap(clazz);
    }

    @Override
    @NotNull
    public Set<T> findAllAndMap() throws InvalidContentException {
        return getRepository().findAllAndMap();
    }

    @Override
    @NotNull
    public SerializedModel save(@NotNull String name, @NotNull T model) {
        return getRepository().save(name, model);
    }

    @Override
    public CompletableFuture<T> findAndMapAsync(String name) {
        return getRepository().findAndMapAsync(name);
    }

    @Override
    public <S extends T> CompletableFuture<S> findAndMapAsync(String name, Class<S> clazz) {
        return getRepository().findAndMapAsync(name, clazz);
    }

    @Override
    public <S extends T> CompletableFuture<Set<S>> findAllAndMapAsync(Class<S> clazz) {
        return getRepository().findAllAndMapAsync(clazz);
    }

    @Override
    public CompletableFuture<Set<T>> findAllAndMapAsync() {
        return getRepository().findAllAndMapAsync();
    }

    @Override
    public CompletableFuture<Boolean> removeAsync(String name) {
        return getRepository().removeAsync(name);
    }

    @Override
    public CompletableFuture<?> saveAsync(String name, T model) {
        return getRepository().saveAsync(name, model);
    }
}
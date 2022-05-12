package com.github.imthenico.repository;

import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AsyncRepository<T> implements Repository<T> {

    private final Executor executor;

    public AsyncRepository(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<T> findAndMapAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findAndMap(name);
            } catch (NoModelSuchException | InvalidContentException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public <S extends T> CompletableFuture<S> findAndMapAsync(String name, Class<S> clazz) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findAndMap(name, clazz);
            } catch (NoModelSuchException | InvalidContentException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public <S extends T> CompletableFuture<Set<S>> findAllAndMapAsync(Class<S> clazz) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findAllAndMap(clazz);
            } catch (InvalidContentException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Set<T>> findAllAndMapAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return findAllAndMap();
            } catch (InvalidContentException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }

    public CompletableFuture<Boolean> removeAsync(String name) {
        return CompletableFuture.supplyAsync(() -> remove(name), executor);
    }

    public CompletableFuture<?> saveAsync(String name, T model) {
        return CompletableFuture.runAsync(() -> save(name, model), executor);
    }
}
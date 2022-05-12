package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.fastannihilation.repository.impl.AtomicDelegatedRepository;
import com.google.gson.JsonObject;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicAnniStorage implements AnniStorage {

    private final AtomicReference<AbstractRepository<JsonObject>> atomicDataRepository = new AtomicReference<>();
    private final AtomicReference<StorageSource> atomicStorageSource = new AtomicReference<>();

    @Override
    public AbstractRepository<JsonObject> getModelDataRepository() {
        return new AtomicDelegatedRepository<>(atomicDataRepository);
    }

    @Override
    public StorageSource getStorageSource() {
        return new AtomicStorageSource(atomicStorageSource);
    }

    public void consume(AnniStorage anniStorage) {
        atomicDataRepository.set(anniStorage.getModelDataRepository());
        atomicStorageSource.set(anniStorage.getStorageSource());
    }
}
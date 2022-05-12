package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.repository.Repository;
import com.google.gson.JsonObject;

public class SimpleAnniStorage implements AnniStorage {

    private final AbstractRepository<JsonObject> mapModelDataRepository;
    private final StorageSource storageSource;

    public SimpleAnniStorage(
            AbstractRepository<JsonObject> mapModelDataRepository,
            StorageSource storageSource
    ) {
        this.mapModelDataRepository = mapModelDataRepository;
        this.storageSource = storageSource;
    }

    @Override
    public AbstractRepository<JsonObject> getModelDataRepository() {
        return mapModelDataRepository;
    }

    @Override
    public StorageSource getStorageSource() {
        return storageSource;
    }
}
package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.repository.AbstractRepository;
import com.google.gson.JsonObject;

public interface AnniStorage {

    AbstractRepository<JsonObject> getModelDataRepository();

    StorageSource getStorageSource();

}
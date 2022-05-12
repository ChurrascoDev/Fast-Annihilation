package com.github.imthenico.fastannihilation.delegate;

import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.repository.AbstractRepository;
import com.google.gson.JsonObject;

import java.util.function.Supplier;

public interface DelegatingFactory {

    static AnniStorage newStorageHandler(
            Supplier<AnniStorage> delegate
    ) {
        return new AnniStorage() {
            @Override
            public AbstractRepository<JsonObject> getModelDataRepository() {
                return delegate.get().getModelDataRepository();
            }

            @Override
            public StorageSource getStorageSource() {
                return delegate.get().getStorageSource();
            }
        };
    }
}
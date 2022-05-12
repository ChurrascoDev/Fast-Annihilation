package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.annihilation.api.storage.StorageSourceType;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicStorageSource extends StorageSource {

    private final AtomicReference<StorageSource> atomicDelegated;

    public AtomicStorageSource(AtomicReference<StorageSource> atomicDelegated) {
        super(null, null);
        this.atomicDelegated = atomicDelegated;
    }

    @Override
    public <T> T getHandle() {
        return atomicDelegated.get().getHandle();
    }

    @Override
    public StorageSourceType getType() {
        return atomicDelegated.get().getType();
    }
}
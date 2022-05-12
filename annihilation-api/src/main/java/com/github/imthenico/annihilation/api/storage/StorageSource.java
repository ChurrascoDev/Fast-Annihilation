package com.github.imthenico.annihilation.api.storage;

public class StorageSource {

    private final Object handle;
    private final StorageSourceType type;

    public StorageSource(Object handle, StorageSourceType type) {
        this.handle = handle;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public <T> T getHandle() {
        return (T) handle;
    }

    public StorageSourceType getType() {
        return type;
    }
}
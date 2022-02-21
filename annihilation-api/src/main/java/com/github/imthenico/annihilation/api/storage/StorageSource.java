package com.github.imthenico.annihilation.api.storage;

public class StorageSource {

    private final StorageSourceType type;
    private final Object handle;

    public StorageSource(StorageSourceType type, Object handle) {
        this.type = type;
        this.handle = handle;
    }

    @SuppressWarnings("unchecked")
    public <T> T getHandle() {
        return (T) handle;
    }

    public StorageSourceType getType() {
        return type;
    }
}
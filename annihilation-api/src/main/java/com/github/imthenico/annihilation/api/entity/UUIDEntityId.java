package com.github.imthenico.annihilation.api.entity;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Objects;
import java.util.UUID;

public class UUIDEntityId extends EntityId {

    private final UUID uuid;

    public UUIDEntityId(UUID uuid) {
        this.uuid = Validate.notNull(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UUIDEntityId that = (UUIDEntityId) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
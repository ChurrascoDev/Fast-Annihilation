package com.github.imthenico.annihilation.api.entity;

import com.github.imthenico.annihilation.api.util.SafeCast;
import org.bukkit.entity.Entity;

import java.util.Objects;
import java.util.function.Supplier;

public class EntityWrapper {

    private final Supplier<Entity> bukkitEntity;
    private final Object handle;

    private EntityWrapper(
            Supplier<Entity> bukkitEntity,
            Object handle
    ) {
        this.bukkitEntity = bukkitEntity;
        this.handle = handle;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T getBukkitEntity() {
        return (T) bukkitEntity.get();
    }

    @SuppressWarnings("unchecked")
    public <T> T getHandle() {
        return (T) handle;
    }

    public SafeCast<?> handle() {
        return SafeCast.of(handle);
    }

    public SafeCast<Entity> bukkitEntity() {
        return SafeCast.of(bukkitEntity.get());
    }

    public void remove() {
        Entity bukkitEntity = getBukkitEntity();

        if (!bukkitEntity.isDead())
            bukkitEntity.remove();
    }

    public static EntityWrapper of(
            Entity bukkitEntity,
            Object handle
    ) {
        Objects.requireNonNull(bukkitEntity);
        return new EntityWrapper(() -> bukkitEntity, Objects.requireNonNull(handle));
    }

    public static EntityWrapper of(
            Supplier<Entity> bukkitEntity,
            Object handle
    ) {
        return new EntityWrapper(
                Objects.requireNonNull(bukkitEntity),
                Objects.requireNonNull(handle)
        );
    }
}
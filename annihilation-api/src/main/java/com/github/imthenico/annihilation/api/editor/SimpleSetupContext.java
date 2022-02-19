package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleSetupContext<T extends ConfigurableModel> implements SetupContext<T> {

    private final T target;
    private final Map<UUID, AnniPlayer> editors;
    private final PropertiesContainer changes;

    public SimpleSetupContext(T target) {
        this(target, target.getProperties());
    }

    public SimpleSetupContext(T target, PropertiesContainer propertiesContainer) {
        this.target = target;
        this.changes = propertiesContainer.copy();
        this.editors = new HashMap<>();
    }

    @Override
    public T getEditingTarget() {
        return target;
    }

    @Override
    public Map<UUID, AnniPlayer> getEditors() {
        return Collections.unmodifiableMap(editors);
    }

    @Override
    public void addEditor(AnniPlayer anniPlayer) throws IllegalArgumentException {
        Validate.isTrue(!editors.containsKey(anniPlayer.getId()), "Player is already editing");

        this.editors.put(anniPlayer.getId(), anniPlayer);
    }

    @Override
    public void removeEditor(AnniPlayer anniPlayer) throws IllegalArgumentException {
        Validate.isTrue(editors.containsKey(anniPlayer.getId()), "Player is not editing this map");

        this.editors.remove(anniPlayer.getId());
    }

    @Override
    public PropertiesContainer getChangesProduced() {
        return changes;
    }
}
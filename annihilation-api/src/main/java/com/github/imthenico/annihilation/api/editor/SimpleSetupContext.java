package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.map.data.EditableMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleSetupContext<T extends EditableMapData> implements SetupContext<T> {

    private final MapModel<T> target;
    private final T data;
    private final Map<UUID, AnniPlayer> editors;

    @SuppressWarnings("unchecked")
    public SimpleSetupContext(MapModel<T> target) {
        this.target = target;
        this.data = (T) target.getData().copy();
        this.editors = new HashMap<>();
    }

    @Override
    public MapModel<T> getEditingTarget() {
        return target;
    }

    @Override
    public Map<UUID, AnniPlayer> getEditors() {
        return Collections.unmodifiableMap(editors);
    }

    @Override
    public void addEditor(AnniPlayer anniPlayer) throws IllegalArgumentException {
        this.editors.put(anniPlayer.getId(), anniPlayer);
    }

    @Override
    public void removeEditor(AnniPlayer anniPlayer) throws IllegalArgumentException {
        this.editors.remove(anniPlayer.getId());
    }

    @Override
    public T getChangesProduced() {
        return data;
    }
}
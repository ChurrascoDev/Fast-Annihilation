package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.map.data.EditableMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;

import java.util.Map;
import java.util.UUID;

public interface SetupContext<T extends EditableMapData> {

    MapModel<T> getEditingTarget();

    Map<UUID, AnniPlayer> getEditors();

    void addEditor(AnniPlayer anniPlayer) throws IllegalArgumentException;

    void removeEditor(AnniPlayer anniPlayer) throws IllegalArgumentException;

    T getChangesProduced();

}
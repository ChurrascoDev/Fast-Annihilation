package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;

import java.util.Map;
import java.util.UUID;

public interface SetupContext {

    ConfigurableModel getEditingTarget();

    Map<UUID, AnniPlayer> getEditors();

    void addEditor(AnniPlayer anniPlayer) throws IllegalArgumentException;

    void removeEditor(AnniPlayer anniPlayer) throws IllegalArgumentException;

    PropertiesContainer getChangesProduced();

}
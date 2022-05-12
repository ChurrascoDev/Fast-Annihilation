package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.event.setup.PlayerLeaveSetupEvent;
import com.github.imthenico.annihilation.api.model.map.data.EditableMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SimpleAbstractSetupManager implements ModelSetupManager {

    private final Map<MapModel<?>, SetupContext<?>> activeSessions;
    private final Map<Class<?>, EditorConfigurator> configurators;

    public SimpleAbstractSetupManager() {
        this.activeSessions = new HashMap<>();
        this.configurators = new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EditableMapData> SetupContext<T> getSession(MapModel<?> model) {
        return (SetupContext<T>) activeSessions.get(model);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EditableMapData> SetupContext<T> getSession(Player player) {
        for (SetupContext<?> value : activeSessions.values()) {
            if (value.getEditors().containsKey(player.getUniqueId()))
                return (SetupContext<T>) value;
        }

        return null;
    }

    @Override
    public <T extends EditableMapData> SetupContext<T> removePlayerFromSession(AnniPlayer player) {
        SetupContext<T> setupContext = getSession(player);

        if (setupContext == null)
            return null;

        setupContext.removeEditor(player);

        if (setupContext.getEditors().size() == 0) {
            activeSessions.remove(setupContext.getEditingTarget());
        }

        Bukkit.getPluginManager().callEvent(new PlayerLeaveSetupEvent(player, setupContext, setupContext.getEditors().size() > 0));

        return setupContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EditableMapData> SetupContext<T> setupModel(
            AnniPlayer player,
            MapModel<T> model
    ) throws UnsupportedOperationException {
        if (getSession(player) != null) {
            throw new UnsupportedOperationException("Player is already editing a map");
        }

        SetupContext<T> setupContext = (SetupContext<T>) activeSessions
                .computeIfAbsent(Objects.requireNonNull(model), (k) -> new SimpleSetupContext<>(model));

        setupContext.addEditor(player);

        EditorConfigurator configurator = configurators.get(model.getDataType());

        if (configurator != null) {
            configurator.configure(player, model);
        }

        return setupContext;
    }

    @Override
    public boolean terminateSession(MapModel<?> model) {
        if (!activeSessions.containsKey(model))
            return false;

        SetupContext<?> setupContext = activeSessions.get(model);

        setupContext.getEditors().values().forEach(setupContext::removeEditor);

        activeSessions.remove(model);
        return true;
    }

    @Override
    public void addConfigurator(Class<? extends EditableMapData> dataType, EditorConfigurator configurator) {
        this.configurators.put(Objects.requireNonNull(dataType), Objects.requireNonNull(configurator));
    }
}
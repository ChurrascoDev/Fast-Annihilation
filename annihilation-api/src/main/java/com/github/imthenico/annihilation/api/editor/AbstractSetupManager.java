package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSetupManager<T extends ConfigurableModel> implements SetupManager<T> {

    private final Map<T, SetupContext<T>> activeSessions;

    public AbstractSetupManager() {
        this.activeSessions = new HashMap<>();
    }

    @Override
    public SetupContext<T> getSession(T model) {
        return activeSessions.get(model);
    }

    @Override
    public SetupContext<T> getSession(Player player) {
        for (SetupContext<T> value : activeSessions.values()) {
            if (value.getEditors().containsKey(player.getUniqueId()))
                return value;
        }

        return null;
    }

    @Override
    public SetupContext<T> setupMap(AnniPlayer player, T map) throws UnsupportedOperationException {
        if (getSession(player) != null) {
            throw new UnsupportedOperationException("Player is already editing a map");
        }

        SetupContext<T> setupContext = activeSessions
                .computeIfAbsent(map, (k) -> new SimpleSetupContext<>(Validate.notNull(map)));

        setupContext.addEditor(player);

        return setupContext;
    }

    @Override
    public boolean terminateSession(T model) {
        if (!activeSessions.containsKey(model))
            return false;

        SetupContext<T> setupContext = activeSessions.get(model);

        setupContext.getEditors().values().forEach(setupContext::removeEditor);

        activeSessions.remove(model);
        return true;
    }
}
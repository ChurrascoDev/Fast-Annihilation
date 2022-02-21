package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSetupManager implements SetupManager {

    private final Map<ConfigurableModel, SetupContext> activeSessions;

    public AbstractSetupManager() {
        this.activeSessions = new HashMap<>();
    }

    @Override
    public SetupContext getSession(ConfigurableModel model) {
        return activeSessions.get(model);
    }

    @Override
    public SetupContext getSession(Player player) {
        for (SetupContext value : activeSessions.values()) {
            if (value.getEditors().containsKey(player.getUniqueId()))
                return value;
        }

        return null;
    }

    @Override
    public SetupContext setupMap(AnniPlayer player, ConfigurableModel model) throws UnsupportedOperationException {
        if (getSession(player) != null) {
            throw new UnsupportedOperationException("Player is already editing a map");
        }

        Validate.notNull(model);

        SetupContext setupContext = activeSessions
                .computeIfAbsent(model, (k) -> new SimpleSetupContext<>(model));

        setupContext.addEditor(player);

        return setupContext;
    }

    @Override
    public boolean terminateSession(ConfigurableModel model) {
        if (!activeSessions.containsKey(model))
            return false;

        SetupContext setupContext = activeSessions.get(model);

        setupContext.getEditors().values().forEach(setupContext::removeEditor);

        activeSessions.remove(model);
        return true;
    }
}
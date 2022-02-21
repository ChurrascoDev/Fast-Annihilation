package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface SetupManager {

    SetupContext getSession(ConfigurableModel model);

    SetupContext getSession(Player player);

    default SetupContext getSession(AnniPlayer player) {
        return getSession(player.getPlayer());
    }

    SetupContext setupMap(AnniPlayer player, ConfigurableModel map) throws UnsupportedOperationException;

    boolean terminateSession(ConfigurableModel model);

    CompletableFuture<?> saveChanges(ConfigurableModel model) throws IllegalArgumentException;

}
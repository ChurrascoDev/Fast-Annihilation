package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public interface SetupManager<T extends ConfigurableModel> {

    SetupContext<T> getSession(T model);

    SetupContext<T> getSession(Player player);

    default SetupContext<T> getSession(AnniPlayer player) {
        return getSession(player.getPlayer());
    }

    SetupContext<T> setupMap(AnniPlayer player, T map) throws UnsupportedOperationException;

    boolean terminateSession(T model);

    CompletableFuture<?> saveChanges(T model) throws IllegalArgumentException;

}
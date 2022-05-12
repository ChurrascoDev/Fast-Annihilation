package com.github.imthenico.annihilation.api.event.setup;

import com.github.imthenico.annihilation.api.editor.SetupContext;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveSetupEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final AnniPlayer player;
    private final SetupContext<?> setupContext;
    private final boolean stillActive;

    public PlayerLeaveSetupEvent(AnniPlayer player, SetupContext<?> setupContext, boolean stillActive) {
        this.player = player;
        this.setupContext = setupContext;
        this.stillActive = stillActive;
    }

    public AnniPlayer getPlayer() {
        return player;
    }

    public SetupContext<?> getSetupContext() {
        return setupContext;
    }

    public boolean isStillActive() {
        return stillActive;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import org.bukkit.event.HandlerList;

public class NexusDestroyEvent extends NexusDamageEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public NexusDestroyEvent(MatchMap matchMap, Nexus nexus, MatchPlayer externAgent, int damage) {
        super(matchMap, nexus, externAgent, damage);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
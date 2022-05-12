package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.HandlerList;

public class NexusDestroyEvent extends NexusDamageEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public NexusDestroyEvent(Match match, Nexus nexus, MatchPlayer externAgent, int damage) {
        super(match, nexus, externAgent, damage);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
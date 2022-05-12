package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.HandlerList;

public class NexusDamageEvent extends NexusEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private int damage;

    public NexusDamageEvent(Match match, Nexus nexus, MatchPlayer externAgent, int damage) {
        super(match, nexus, externAgent);
        this.damage = damage;
    }

    public void setDamage(int damage) {
        if (damage < 0)
            return;

        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
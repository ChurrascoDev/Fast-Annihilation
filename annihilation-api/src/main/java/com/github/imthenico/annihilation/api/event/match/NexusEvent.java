package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NexusEvent extends MatchEvent {

    private final Nexus nexus;
    private final MatchPlayer externAgent;

    public NexusEvent(Match match, Nexus nexus, MatchPlayer externAgent) {
        super(match);
        this.nexus = nexus;
        this.externAgent = externAgent;
    }

    public @NotNull Nexus getNexus() {
        return nexus;
    }

    public @Nullable MatchPlayer getExternalAgent() {
        return externAgent;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
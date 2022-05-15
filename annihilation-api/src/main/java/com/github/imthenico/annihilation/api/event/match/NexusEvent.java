package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class NexusEvent extends Event {

    private final Nexus nexus;
    private final MatchMap matchMap;
    private final MatchPlayer externAgent;

    public NexusEvent(MatchMap matchMap, Nexus nexus, MatchPlayer externAgent) {
        this.nexus = nexus;
        this.externAgent = externAgent;
        this.matchMap = matchMap;
    }

    public @NotNull Nexus getNexus() {
        return nexus;
    }

    public @Nullable MatchPlayer getExternalAgent() {
        return externAgent;
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }
}
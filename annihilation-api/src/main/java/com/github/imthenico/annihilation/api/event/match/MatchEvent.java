package com.github.imthenico.annihilation.api.event.match;

import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class MatchEvent extends Event {

    private final Match match;

    public MatchEvent(Match match) {
        this.match = Objects.requireNonNull(match);
    }

    public @NotNull Match getMatch() {
        return match;
    }

    public @NotNull String getMatchTypeName() {
        return match.getTypeName();
    }

    public boolean isOfType(String expectedType) {
        return match.getTypeName().equals(expectedType);
    }

}
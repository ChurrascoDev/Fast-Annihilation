package com.github.imthenico.annihilation.api.event.game;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public abstract class GameEvent extends Event {

    private final Game game;

    public GameEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public @Nullable Match runningMatch() {
        return game.runningMatch();
    }

    public boolean isOfType(String expectedTypeName) {
        return game.getTypeName().equals(expectedTypeName);
    }
}
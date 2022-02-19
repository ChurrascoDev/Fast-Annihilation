package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleGameInstanceManager implements GameInstanceManager {

    private final Map<String, GameInstance> gameRooms;

    public SimpleGameInstanceManager() {
        this.gameRooms = new HashMap<>();
    }

    @Override
    public GameInstance getGame(String id) {
        return gameRooms.get(id);
    }

    @Override
    public void registerGame(GameInstance gameInstance) {
        Validate.isTrue(!gameRooms.containsKey(gameInstance.id()), "There's already a registered game with this is");

        gameRooms.put(gameInstance.id(), gameInstance);
    }

    @Override
    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(gameRooms.keySet());
    }

    @Override
    public GameInstance setEnabled(String id, boolean enabled) {
        GameInstance gameInstance = getGame(id);

        if (gameInstance != null)
            gameInstance.setEnabled(enabled);

        return gameInstance;
    }

    @Override
    public boolean isEnabled(String id) {
        GameInstance gameInstance = getGame(id);

        if (gameInstance != null)
            return gameInstance.isEnabled();

        return false;
    }

    @NotNull
    @Override
    public Iterator<GameInstance> iterator() {
        return new UnmodifiableIterator<>(gameRooms.values().iterator());
    }
}
package com.github.imthenico.fastannihilation.game;

import com.github.imthenico.annihilation.api.game.GameManager;
import com.github.imthenico.annihilation.api.game.GameRoom;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleGameManager implements GameManager {

    private final Map<String, GameRoom> gameRooms;

    public SimpleGameManager() {
        this.gameRooms = new HashMap<>();
    }

    @Override
    public GameRoom getGame(String id) {
        return gameRooms.get(id);
    }

    @Override
    public void registerGame(GameRoom gameRoom) {
        if (gameRooms.containsKey(gameRoom.id())) {
            throw new IllegalArgumentException("There's already a registered game with this is");
        }

        gameRooms.put(gameRoom.id(), gameRoom);
    }

    @Override
    public Set<String> getIdentifiers() {
        return Collections.unmodifiableSet(gameRooms.keySet());
    }

    @Override
    public GameRoom setEnabled(String id, boolean enabled) {
        GameRoom gameRoom = getGame(id);

        if (gameRoom != null)
            gameRoom.setEnabled(enabled);

        return gameRoom;
    }

    @Override
    public boolean isEnabled(String id) {
        GameRoom gameRoom = getGame(id);

        if (gameRoom != null)
            return gameRoom.isEnabled();

        return false;
    }

    @Override
    public boolean isRegistered(String id) {
        return gameRooms.containsKey(id);
    }

    @NotNull
    @Override
    public Iterator<GameRoom> iterator() {
        Iterator<GameRoom> iterator = gameRooms.values().iterator();

        return new Iterator<GameRoom>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public GameRoom next() {
                return iterator.next();
            }
        };
    }
}
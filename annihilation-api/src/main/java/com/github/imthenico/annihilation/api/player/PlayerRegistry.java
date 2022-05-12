package com.github.imthenico.annihilation.api.player;

import java.util.*;

public class PlayerRegistry implements Iterable<AnniPlayer> {

    private final Map<UUID, AnniPlayer> playerMap;

    public PlayerRegistry() {
        this.playerMap = new HashMap<>();
    }

    public AnniPlayer getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }

    public void registerPlayer(AnniPlayer player) {
        if (exists(player.getId())) {
            throw new IllegalArgumentException("This player is already registered.");
        }

        this.playerMap.put(player.getId(), player);
    }

    public boolean exists(UUID uuid) {
        return playerMap.containsKey(uuid);
    }

    @Override
    public Iterator<AnniPlayer> iterator() {
        Iterator<AnniPlayer> iterator = playerMap.values().iterator();

        return new Iterator<AnniPlayer>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public AnniPlayer next() {
                return iterator.next();
            }
        };
    }
}
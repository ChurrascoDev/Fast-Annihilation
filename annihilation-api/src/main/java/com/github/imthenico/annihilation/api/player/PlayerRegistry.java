package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.*;

public class PlayerRegistry implements Iterable<AnniPlayer> {

    private final Map<UUID, AnniPlayer> playerMap;

    public PlayerRegistry() {
        this.playerMap = new HashMap<>();
    }

    public Optional<AnniPlayer> getPlayer(UUID uuid) {
        return Optional.ofNullable(playerMap.get(uuid));
    }

    public void registerPlayer(AnniPlayer player) {
        Validate.isTrue(!exists(player.getId()), "This player is already registered.");

        this.playerMap.put(player.getId(), player);
    }

    public boolean exists(UUID uuid) {
        return playerMap.containsKey(uuid);
    }

    @Override
    public Iterator<AnniPlayer> iterator() {
        return new UnmodifiableIterator<>(playerMap.values().iterator());
    }
}
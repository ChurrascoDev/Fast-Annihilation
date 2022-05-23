package com.github.imthenico.annihilation.api.model.lobby;

import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.eventbus.key.Key;
import com.github.imthenico.gmlib.GameMap;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.atomic.AtomicReference;

public class GameLobby extends GameMap {

    private final AtomicReference<LocationModel> spawn;

    public GameLobby(GameLobbyData modelData) {
        this.spawn = new AtomicReference<>(modelData.getSpawn());

        modelData.subscribe(
                Key.of("lobby-data-listener"),
                dataMutateEvent -> {
                    if (dataMutateEvent.getMutationAction().equals("set_spawn")) {
                        GameLobby.this.spawn.set((LocationModel) dataMutateEvent.getMutatedData());
                    }
                }
        );
    }

    public Location getSpawn() {
        LocationModel locationModel = spawn.get();

        LocationModel copy = locationModel.copy();
        copy.setWorldName(null);

        if (LocationModel.ZERO.equals(copy))
            return ((World) getMainWorld().getHandle()).getSpawnLocation();

        return locationModel.toBukkit(allWorlds());
    }
}
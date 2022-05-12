package com.github.imthenico.annihilation.api.model.lobby;

import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.eventbus.key.Key;
import com.github.imthenico.gmlib.GameMap;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldContainer;
import com.github.imthenico.inject.annotation.InjectAll;
import com.github.imthenico.inject.annotation.Skip;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.concurrent.atomic.AtomicReference;

public class GameLobby extends GameMap {

    private final AtomicReference<LocationModel> spawn;

    @InjectAll(from = GameLobbyData.class, valuesToExtract = {})
    public GameLobby(
            @Skip GameLobbyData modelData,
            @Skip AWorld mainWorld,
            @Skip WorldContainer additionalWorlds,
            @Skip String mapName
    ) {
        super(modelData, mainWorld, additionalWorlds, mapName);

        this.spawn = new AtomicReference<>();

        setSpawn(modelData.getSpawn());

        modelData.subscribe(
                Key.of("lobby-data-listener"),
                dataMutateEvent -> {
                    if (dataMutateEvent.getMutationAction().equals("setspawn")) {
                        GameLobby.this.spawn.set((LocationModel) dataMutateEvent.getMutatedData());
                    }
                }
        );
    }

    private void setSpawn(LocationModel locationModel) {
        if (locationModel.getWorldName() == null) {
            locationModel.setWorldName(getMainWorld().getName());
        }

        this.spawn.set(locationModel);
    }

    public Location getSpawn() {
        LocationModel locationModel = spawn.get();

        LocationModel copy = locationModel.copy();
        copy.setWorldName(null);

        if (LocationModel.ZERO.equals(copy))
            return ((World) getMainWorld().handle()).getSpawnLocation();

        return locationModel.toBukkit(allWorlds());
    }
}
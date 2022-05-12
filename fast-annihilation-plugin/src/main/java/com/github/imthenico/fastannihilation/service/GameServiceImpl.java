package com.github.imthenico.fastannihilation.service;

import com.github.imthenico.annihilation.api.game.GameFactory;
import com.github.imthenico.annihilation.api.game.GameManager;
import com.github.imthenico.fastannihilation.game.SimpleGameManager;
import com.github.imthenico.fastannihilation.game.SimpleGameRoomFactory;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.gmlib.GameMapHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class GameServiceImpl implements GameService {

    private final GameManager gameManager;
    private final GameFactory gameFactory;
    private LocationReference spawnReference;

    public GameServiceImpl(
            UtilityPack utilityPack,
            Location lobbySpawn,
            ModelCache modelCache,
            GameMapHandler mapHandler
    ) {
        this.spawnReference = () -> {
            if (lobbySpawn != null)
                return lobbySpawn;

            return Bukkit.getWorlds().get(0).getSpawnLocation();
        };

        this.gameManager = new SimpleGameManager();
        this.gameFactory = new SimpleGameRoomFactory(
                modelCache,
                mapHandler,
                utilityPack,
                spawnReference
        );
    }

    @Override
    public GameManager gameManager() {
        return gameManager;
    }

    @Override
    public GameFactory factory() {
        return gameFactory;
    }

    @Override
    public LocationReference getLobbySpawnReference() {
        return spawnReference;
    }

    @Override
    public void setLobbySpawnReference(LocationReference spawnReference) {
        this.spawnReference = spawnReference;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }
}
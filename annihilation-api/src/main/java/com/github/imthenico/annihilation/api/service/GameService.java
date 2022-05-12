package com.github.imthenico.annihilation.api.service;

import com.github.imthenico.annihilation.api.game.GameFactory;
import com.github.imthenico.annihilation.api.game.GameManager;
import com.github.imthenico.annihilation.api.world.LocationReference;

public interface GameService extends Service {

    GameManager gameManager();

    GameFactory factory();

    LocationReference getLobbySpawnReference();

    void setLobbySpawnReference(LocationReference spawnReference);
}
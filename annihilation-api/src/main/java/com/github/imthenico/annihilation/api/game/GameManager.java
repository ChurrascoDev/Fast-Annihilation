package com.github.imthenico.annihilation.api.game;

import java.util.Set;

public interface GameManager extends Iterable<GameRoom> {

    GameRoom getGame(String id);

    void registerGame(GameRoom gameRoom);

    Set<String> getIdentifiers();

    GameRoom setEnabled(String id, boolean enabled);

    boolean isEnabled(String id);

    boolean isRegistered(String id);

}
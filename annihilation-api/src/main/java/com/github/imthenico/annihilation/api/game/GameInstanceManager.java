package com.github.imthenico.annihilation.api.game;

import java.util.Set;

public interface GameInstanceManager extends Iterable<GameInstance> {

    GameInstance getGame(String id);

    void registerGame(GameInstance gameInstance);

    Set<String> getIdentifiers();

    GameInstance setEnabled(String id, boolean enabled);

    boolean isEnabled(String id);

}
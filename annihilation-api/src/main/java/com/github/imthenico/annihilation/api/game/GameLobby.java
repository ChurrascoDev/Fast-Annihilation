package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.LoadedModel;

public interface GameLobby extends LoadedModel {

    GameInstance getGame();

}
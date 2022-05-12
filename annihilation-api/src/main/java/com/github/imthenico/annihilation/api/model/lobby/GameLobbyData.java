package com.github.imthenico.annihilation.api.model.lobby;

import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.annihilation.api.model.map.data.EditableMapData;

public class GameLobbyData extends EditableMapData {

    private LocationModel spawn;

    public GameLobbyData() {
        this.spawn = LocationModel.ZERO;
    }

    public GameLobbyData(LocationModel spawn) {
        this.spawn = spawn;
    }

    @Override
    public boolean editable() {
        return true;
    }

    @Override
    public void accept(EditableMapData modelData) throws UnsupportedOperationException {
        if (!(modelData instanceof GameLobbyData))
            throw new IllegalArgumentException("Invalid model data");

        GameLobbyData lobbyModelData = (GameLobbyData) modelData;
        this.spawn = lobbyModelData.spawn;
    }

    @Override
    public GameLobbyData copy() {
        GameLobbyData lobbyModelData = new GameLobbyData();

        if (spawn != null) {
            lobbyModelData.setSpawn(spawn);
        }

        return this;
    }

    public void setSpawn(LocationModel spawn) {
        this.spawn = spawn.copy();
    }

    public LocationModel getSpawn() {
        return spawn;
    }
}
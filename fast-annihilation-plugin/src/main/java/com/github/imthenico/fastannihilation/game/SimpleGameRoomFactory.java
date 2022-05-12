package com.github.imthenico.fastannihilation.game;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.game.GameExpansion;
import com.github.imthenico.annihilation.api.game.GameFactory;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.lobby.GameLobby;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.fastannihilation.match.DefaultMatchFactory;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.inject.ConstructorModel;
import com.github.imthenico.inject.InjectionContext;
import com.github.imthenico.inject.InjectionStructure;
import me.yushust.message.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SimpleGameRoomFactory implements GameFactory {

    private final Map<String, GameExpansion> matchConfigurationMap = new HashMap<>();
    private final MatchFactory matchFactory;
    private final GameMapHandler mapHandler;
    private final LocationReference spawnReference;
    private final InjectionStructure lobbyInjectionStructure = AnnihilationAPI.INJECTION_HANDLER
            .createStructure(ConstructorModel.fromClass(GameLobby.class));

    public SimpleGameRoomFactory(
            ModelCache modelCache,
            GameMapHandler mapHandler,
            UtilityPack utilityPack,
            LocationReference spawnReference
    ) {
        this.mapHandler = mapHandler;
        this.matchFactory = new DefaultMatchFactory(mapHandler);
        MessageHandler messageHandler = utilityPack.getMessageHandler();
        registerExpansion("default", new DefaultGameExpansion(messageHandler, modelCache));
        this.spawnReference = spawnReference;
    }

    @Override
    public GameRoom newGame(
            String id,
            String typeName,
            MapModel<? extends GameLobbyData> lobbyModel
    ) throws IllegalArgumentException {
        if (typeName == null)
            typeName = "default";

        GameExpansion expansion = matchConfigurationMap.get(typeName);

        if (expansion == null) {
            throw new IllegalArgumentException("No expansion found for '" + typeName + "'");
        }

        GameLobby gameLobby = mapHandler.createMap(
                lobbyModel,
                new InjectionContext(lobbyInjectionStructure),
                id
        );

        return new SimpleGameRoom(
                gameLobby,
                typeName,
                spawnReference,
                id,
                expansion.newRules(),
                expansion.newOptions(),
                expansion.newAuthorizer(),
                matchFactory,
                expansion.getMatchExpansion(),
                expansion.newMatchMapModelProvider(),
                expansion.getMapCandidateValidator()
        );
    }

    @Override
    public GameExpansion getConfiguration(String typeName) {
        return matchConfigurationMap.get(typeName);
    }

    @Override
    public void registerExpansion(String typeName, GameExpansion gameExpansion) {
        matchConfigurationMap.put(
                Objects.requireNonNull(typeName),
                Objects.requireNonNull(gameExpansion)
        );
    }
}
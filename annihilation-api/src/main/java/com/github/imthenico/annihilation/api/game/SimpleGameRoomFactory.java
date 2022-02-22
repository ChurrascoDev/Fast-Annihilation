package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.cache.ConfigurableModelCache;
import com.github.imthenico.annihilation.api.converter.ConverterToGameLobby;
import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.SimpleMatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleGameRoomFactory implements GameFactory {

    private final Map<String, MatchFactory> matchCreators;
    private final ConfigurableModelCache modelCache;
    private ModelConverter<GameLobby> toLobbyConverter;

    public SimpleGameRoomFactory(
            UtilityPack utilityPack,
            Scheduler scheduler,
            GameInstanceManager gameInstanceManager,
            ConfigurableModelCache modelCache
    ) {
        this.matchCreators = new HashMap<>();
        matchCreators.put("default", MatchFactory.create(utilityPack, scheduler, "default"));
        this.toLobbyConverter = new ConverterToGameLobby(gameInstanceManager);
        this.modelCache = modelCache;
    }

    @Override
    public GameInstance newGame(
            String id,
            String matchType,
            ConfigurableModel lobbyModel
    ) throws IllegalArgumentException {
        MatchFactory matchFactory;

        if (matchType == null)
            matchType = "default";

        matchFactory = matchCreators.get(matchType);

        Validate.isTrue(matchFactory != null, "Unable to find a match creator");

        return new SimpleGameInstance(
                toLobbyConverter.convert(lobbyModel, Collections.singletonMap("gameId", id)),
                id,
                new SimpleMatchAuthorizer(modelCache),
                matchFactory
        );
    }

    @Override
    public void registerMatchCreator(
            String matchTypeName,
            MatchFactory matchFactory
    ) throws IllegalArgumentException {
        Validate.isTrue(!matchCreators.containsKey(matchTypeName), "There's already a registered match creator with that key");

        matchCreators.put(matchTypeName, Validate.notNull(matchFactory));
    }

    @Override
    public void setToLobbyConverter(ModelConverter<GameLobby> toLobbyConverter) {
        this.toLobbyConverter = Validate.notNull(toLobbyConverter);
    }
}
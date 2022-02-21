package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.converter.ConverterToGameLobby;
import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.SimpleMatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleGameRoomFactory implements GameFactory {

    private final AnnihilationAPI annihilationAPI;
    private final Map<String, MatchFactory> matchCreators;
    private ModelConverter<GameLobby> toLobbyConverter;

    public SimpleGameRoomFactory(
            AnnihilationAPI annihilationAPI,
            GameInstanceManager gameInstanceManager
    ) {
        this.annihilationAPI = Validate.notNull(annihilationAPI);
        this.matchCreators = new HashMap<>();

        matchCreators.put("default", MatchFactory.create(annihilationAPI, "default"));
        this.toLobbyConverter = new ConverterToGameLobby(gameInstanceManager);
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
                GameInstance.DEFAULT_RULES,
                new SimpleMatchAuthorizer(annihilationAPI),
                matchFactory,
                new GameInstance.Options()
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
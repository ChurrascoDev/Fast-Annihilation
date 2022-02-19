package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.Annihilation;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.SimpleMatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.HashMap;
import java.util.Map;

public class SimpleGameRoomFactory implements AnnihilationGameFactory {

    private final Annihilation annihilation;
    private final Map<String, MatchFactory> matchCreators;
    private final
    public SimpleGameRoomFactory(Annihilation annihilation) {
        this.annihilation = Validate.notNull(annihilation);
        this.matchCreators = new HashMap<>();

        matchCreators.put("default", MatchFactory.create(annihilation, "default"));
    }

    @Override
    public GameInstance newGame(
            String matchType,
            ConfigurableModel model, Map<String, String> extraData
    ) throws IllegalArgumentException {
        MatchFactory matchFactory;

        if (matchType == null)
            matchType = "default";

        matchFactory = matchCreators.get(matchType);

        Validate.isTrue(matchFactory != null, "Unable to find a match creator");

        return new SimpleGameInstance(
                new SimpleGameLobby(lobbyWorld),
                matchType,
                GameInstance.DEFAULT_RULES,
                new SimpleMatchAuthorizer(annihilation),
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
}
package com.github.imthenico.annihilation.api.match.authorization;

import com.github.imthenico.annihilation.api.cache.ConfigurableModelCache;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.service.ConfigurableModelService;
import com.github.imthenico.annihilation.api.util.VoteCounter;

import java.util.*;

import static com.github.imthenico.annihilation.api.message.MessagePath.*;
import static com.github.imthenico.annihilation.api.util.GameValidation.*;

public class SimpleMatchAuthorizer implements MatchAuthorizer {

    private final ConfigurableModelCache modelCache;

    public SimpleMatchAuthorizer(ConfigurableModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public AuthorizationResult canStart(GameInstance game) {
        GameInstance.Rules rules = game.getRules();
        VoteCounter<UUID, ConfigurableModel> playerVotes = game.getPreparationStage().getVotes();
        boolean authorized;
        String messagePath;
        String reason = null;

        ConfigurableModel mostVoted = playerVotes.mostVoted();

        if (!hasAvailableMaps(game, modelCache)) {
            messagePath = "match-cannot-start.no-available-maps";
            authorized = false;
            reason = "No available maps";
        } else if (mostVoted == null && rules.getDefaultMap() == null) {
            messagePath = "match-cannot-start.no-map-voted";
            authorized = false;
            reason = "No map voted";
        } else if (!balancedTeams(game)) {
            messagePath = "match-cannot-start.unbalanced-teams";
            authorized = false;
            reason = "Unbalanced Teams";
        } else if (mostVoted != null && !isMapCorrectlyConfigured(mostVoted)) {
            messagePath = "match-cannot-start.map-not-configured";
            authorized = false;
            reason = "Map is not configured correctly";
        } else {
            messagePath = "match-can-start";
            authorized = true;
        }

        return AuthorizationResult.of(authorized, path(messagePath), reason);
    }
}
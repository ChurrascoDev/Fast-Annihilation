package com.github.imthenico.fastannihilation.match;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.Rules;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.gmlib.MapModel;

import java.util.UUID;

import static com.github.imthenico.annihilation.api.message.MessagePath.path;
import static com.github.imthenico.annihilation.api.util.GameValidation.*;

public class DefaultMatchAuthorizer implements MatchAuthorizer {

    private final ModelCache modelCache;

    public DefaultMatchAuthorizer(ModelCache modelCache) {
        this.modelCache = modelCache;
    }

    @Override
    public AuthorizationResult canStart(Game game) {
        Rules rules = game.room().getRules();

        VoteCounter<UUID, String> playerVotes = game.getPreparationStage().getVotes();
        boolean authorized;
        String messagePath;
        String reason = null;

        MapModel<? extends MatchMapData> mostVoted = modelCache.getModel(playerVotes.mostVoted());

        if (!hasAvailableMaps(game, modelCache)) {
            messagePath = "match-cannot-start.no-available-maps";
            authorized = false;
            reason = "No available maps";
        } else if (mostVoted == null && rules.getDefaultMap() == null && !game.getOptions().isSelectRandomMap()) {
            messagePath = "match-cannot-start.no-map-voted";
            authorized = false;
            reason = "No map voted";
        } else if (!balancedTeams(game)) {
            messagePath = "match-cannot-start.unbalanced-teams";
            authorized = false;
            reason = "Unbalanced Teams";
        } else if (mostVoted != null && !isDataCorrectlyConfigured(mostVoted.getData())) {
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
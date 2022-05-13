package com.github.imthenico.fastannihilation.match;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.Rules;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.gmlib.MapModel;

import java.util.UUID;

import static com.github.imthenico.annihilation.api.message.MessagePath.path;
import static com.github.imthenico.annihilation.api.util.GameValidation.*;

public class DefaultMatchAuthorizer implements MatchAuthorizer {

    private final static String SUCCESS_NAMESPACE = "passed_checks";

    @Override
    public AuthorizationResult canStart(Game game) {
        Rules rules = game.room().getRules();

        VoteCounter<UUID, String> playerVotes = game.getPreparationStage().getVotes();
        boolean authorized;
        String messagePath;
        String reason;

        String candidate = playerVotes.mostVoted();

        if (candidate == null && rules.getDefaultMap() == null && !game.getOptions().isSelectRandomMap()) {
            messagePath = "match-cannot-start-no-map-voted";
            authorized = false;
            reason = "no_map_voted";
        } else if (!balancedTeams(game)) {
            messagePath = "match-cannot-start-unbalanced-teams";
            authorized = false;
            reason = "unbalanced_Teams";
        } else {
            messagePath = "match-can-start";
            authorized = true;
            reason = SUCCESS_NAMESPACE;
        }

        return AuthorizationResult.of(authorized, path(messagePath), reason);
    }

    @Override
    public AuthorizationResult isEligibleForMap(MapModel<? extends MatchMapData> model) {
        if (!isDataCorrectlyConfigured(model.getData()))
            return AuthorizationResult.of(false, path("invalid-map-for-match"), "not_configured_data");

        return AuthorizationResult.of(true, path("valid-map-for-match"), SUCCESS_NAMESPACE);
    }
}
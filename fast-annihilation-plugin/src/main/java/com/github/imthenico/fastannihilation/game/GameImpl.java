package com.github.imthenico.fastannihilation.game;

import com.github.imthenico.annihilation.api.game.*;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.util.RandomElementPicker;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.gmlib.MapModel;

import java.util.List;
import java.util.UUID;

public class GameImpl implements Game {

    private final GameRoom owner;
    private final Options options;
    private final Rules rules;
    private final MatchFactory matchFactory;
    private final MatchExpansion matchExpansion;
    private final PreMatchStage preparationStage;
    private final MatchAuthorizer matchAuthorizer;
    private final MatchMapModelProvider matchMapModelProvider;

    private Match match;

    public GameImpl(
            GameRoom owner,
            MatchFactory matchFactory,
            MatchExpansion matchExpansion,
            MatchAuthorizer matchAuthorizer,
            MatchMapModelProvider matchMapModelProvider
    ) {
        this.owner = owner;
        this.options = owner.getOptions().copy();
        this.rules = owner.getRules().copy();
        this.matchFactory = matchFactory;
        this.matchExpansion = matchExpansion;
        this.matchAuthorizer = matchAuthorizer;
        this.matchMapModelProvider = matchMapModelProvider;
        this.preparationStage = new SimplePreMatchStage(rules.getTimeToStart(), this, matchMapModelProvider);
    }

    @Override
    public GameState calculateState() {
        if (match != null) {
            if (match.getClosingStage().isRunning()) {
                return GameState.ENDING;
            }

            return GameState.IN_GAME;
        }

        if (preparationStage.getCountdownToStart().getElapsedTime() > 0) {
            return GameState.STARTING;
        }

        return GameState.WAITING;
    }
    
    @Override
    public AuthorizationResult startMatch() {
        if (match != null) {
            throw new UnsupportedOperationException("There's already a match running.");
        }

        AuthorizationResult result = matchAuthorizer.canStart(this);

        if (result.isAuthorized()) {
            VoteCounter<UUID, String> voteCounter = preparationStage.getVotes();

            String votedMapName = voteCounter.mostVoted();

            if (votedMapName == null) {
                if (options.isSelectRandomMap()) {
                    votedMapName = rules.getDefaultMap();
                } else {
                    List<String> candidates = voteCounter.getCandidates();

                    votedMapName = RandomElementPicker.pickRandom(candidates);
                }
            }

            if (votedMapName == null) {
                throw new RuntimeException("Unable to get a map candidate name");
            }

            MapModel<? extends MatchMapData> mapModel = matchMapModelProvider.getModel(votedMapName);

            AuthorizationResult modelAuthorizationResult = matchAuthorizer.isEligibleForMap(mapModel);

            if (modelAuthorizationResult.isAuthorized()) {
                this.match = matchFactory.createMatch(this, matchExpansion, mapModel);
                match.start();
            }

            return modelAuthorizationResult;
        }

        return result;
    }
    
    @Override
    public Match runningMatch() {
        return match;
    }
    
    @Override
    public PreMatchStage getPreparationStage() {
        return preparationStage;
    }

    @Override
    public MatchAuthorizer getMatchAuthorizer() {
        return matchAuthorizer;
    }
    
    /**
     * This method is used to identify different types of
     * games with different game functions, characteristics, etc.
     */
    public String getTypeName() {
        return owner.getTypeName();
    }

    public Options getOptions() {
        return options;
    }

    public Rules getRules() {
        return rules;
    }

    @Override
    public GameRoom room() {
        return owner;
    }
}
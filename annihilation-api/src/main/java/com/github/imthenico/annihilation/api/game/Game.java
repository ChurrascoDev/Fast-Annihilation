package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.map.data.MatchMapData;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.util.VoteCounter;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;
import com.github.imthenico.gmlib.MapModel;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Game {

    private final GameRoom owner;
    private final Options options;
    private final Rules rules;
    private final MatchFactory matchFactory;
    private final MatchExpansion matchExpansion;
    private final PreMatchStage preparationStage;
    private final MatchAuthorizer matchAuthorizer;
    private final MatchMapModelProvider matchMapModelProvider;

    private Match match;

    public Game(
            GameRoom owner,
            MatchFactory matchFactory,
            MatchExpansion matchExpansion,
            MatchAuthorizer matchAuthorizer,
            MatchMapModelProvider matchMapModelProvider,
            MapCandidateValidator mapCandidateValidator,
            int timeToStart
    ) {
        this.owner = owner;
        this.options = owner.getOptions().copy();
        this.rules = owner.getRules().copy();
        this.matchFactory = matchFactory;
        this.matchExpansion = matchExpansion;
        this.matchAuthorizer = matchAuthorizer;
        this.matchMapModelProvider = matchMapModelProvider;
        this.preparationStage = new SimplePreMatchStage(timeToStart, this, matchMapModelProvider, mapCandidateValidator);
    }

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

    public PreMatchStage getPreparationStage() {
        return preparationStage;
    }

    public AuthorizationResult startMatch() {
        if (match != null) {
            throw new UnsupportedOperationException("There's already a match running.");
        }

        AuthorizationResult result = matchAuthorizer.canStart(this);

        if (result.isAuthorized()) {
            VoteCounter<UUID, String> voteCounter = preparationStage.getVotes();

            String votedMapName = voteCounter.mostVoted();

            if (votedMapName == null && options.isSelectRandomMap()) {
                List<String> candidates = voteCounter.getCandidates();

                int i = ThreadLocalRandom.current().nextInt(candidates.size() - 1);

                votedMapName = candidates.get(i);
            } else {
                throw new RuntimeException("Unable to select a map name");
            }

            MapModel<? extends MatchMapData> mapModel = matchMapModelProvider.getModel(votedMapName);

            this.match = matchFactory.createMatch(this, matchExpansion, mapModel);
            match.start();
        }

        return result;
    }

    public GameRoom room() {
        return owner;
    }

    public Match runningMatch() {
        return match;
    }

    public MatchAuthorizer matchAuthorizer() {
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
}
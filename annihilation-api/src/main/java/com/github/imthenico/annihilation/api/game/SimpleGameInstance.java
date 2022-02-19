package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.*;
import java.util.function.Predicate;

public class SimpleGameInstance implements GameInstance {

    private final GameLobby gameLobby;
    private final String id;
    private final Rules rules;
    private final Set<AnniPlayer> players;
    private final MatchAuthorizer simpleMatchAuthorizer;
    private final MatchFactory matchFactory;
    private final Options options;

    private PreMatchStage preparationStage;
    private Match match;
    private boolean enabled;

    public SimpleGameInstance(
            GameLobby gameLobby,
            String id,
            Rules rules,
            MatchAuthorizer simpleMatchAuthorizer,
            MatchFactory matchFactory,
            Options options
    ) {
        this.gameLobby = Validate.notNull(gameLobby);
        this.id = Validate.notNull(id);
        this.rules = Validate.notNull(rules);
        this.simpleMatchAuthorizer = Validate.notNull(simpleMatchAuthorizer);
        this.matchFactory = Validate.notNull(matchFactory);
        this.options = Validate.defIfNull(options, new Options());
        this.players = new HashSet<>();
        this.preparationStage = new SimplePreMatchStage(rules.getTimeToStart());
    }

    @Override
    public GameLobby getLobby() {
        return gameLobby;
    }

    @Override
    public State getState() {
        if (match != null) {
            if (match.getClosingStage().isRunning()) {
                return State.ENDING;
            }

            return State.IN_GAME;
        }

        if (preparationStage.getCountdownToStart().getElapsedTime() > 0) {
            return State.STARTING;
        }

        return State.WAITING;
    }

    @Override
    public void join(AnniPlayer anniPlayer) {
        GameInstance game = anniPlayer.getPlayingGame();

        if (game != null) {
            if (game == this) {
                return;
            }

            throw new IllegalArgumentException("Player is playing another game.");
        }

        this.players.add(anniPlayer);
        anniPlayer.handleInternalGameJoin(this);
    }

    @Override
    public void leave(AnniPlayer anniPlayer) {
        Validate.isTrue(isInGame(anniPlayer), "That player is not registered in this game instance.");

        this.players.remove(anniPlayer);
        anniPlayer.handleInternalGameLeave(this);
    }

    @Override
    public boolean isInGame(AnniPlayer anniPlayer) {
        return players.contains(anniPlayer);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled && match != null) {
            match.terminate();
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Rules getRules() {
        return rules;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public PreMatchStage getPreparationStage() {
        return preparationStage;
    }

    @Override
    public AuthorizationResult startMatch() {
        Validate.isTrue(match == null, "There's already a match running.");

        AuthorizationResult result = simpleMatchAuthorizer.canStart(this);

        if (result.isAuthorized()) {
            ConfigurableModel mapModel = preparationStage.getVotes().mostVoted();

            this.match = matchFactory.createMatch(this, mapModel);
            match.start();
        }

        return result;
    }

    @Override
    public Match getMatch() {
        return match;
    }

    @Override
    public MatchAuthorizer getMatchAuthorizer() {
        return simpleMatchAuthorizer;
    }

    @Override
    public void discardMatch() {
        Validate.isTrue(match != null, "No match running.");

        if (!match.finalized()) {
            match.terminate();

            this.preparationStage = new SimplePreMatchStage(rules.getTimeToStart());
        }
    }

    @Override
    public List<AnniPlayer> getPlayers(Predicate<AnniPlayer> filter) {
        List<AnniPlayer> anniPlayers = new ArrayList<>(players.size());

        for (AnniPlayer player : players) {
            if (filter.test(player))
                anniPlayers.add(player);
        }

        return anniPlayers;
    }
}
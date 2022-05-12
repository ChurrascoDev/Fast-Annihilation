package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.match.expansion.DefaultMatchExpansion;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.match.authorization.DefaultMatchAuthorizer;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.strategy.DefaultMatchMapModelProvider;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;
import me.yushust.message.MessageHandler;

public class DefaultGameExpansion implements GameExpansion {

    private final MatchExpansion matchExpansion;
    private final MatchAuthorizer matchAuthorizer;
    private final MatchMapModelProvider matchMapModelProvider;

    public DefaultGameExpansion(MessageHandler messageHandler, ModelCache modelCache) {
        this.matchExpansion = new DefaultMatchExpansion(messageHandler);
        this.matchAuthorizer = new DefaultMatchAuthorizer(modelCache);
        this.matchMapModelProvider = new DefaultMatchMapModelProvider(modelCache);
    }

    @Override
    public MatchExpansion getMatchExpansion() {
        return matchExpansion;
    }

    @Override
    public MapCandidateValidator getMapCandidateValidator() {
        return MapCandidateValidator.defaultValidator();
    }

    @Override
    public Options newOptions() {
        return Options.defaultOptions();
    }

    @Override
    public Rules newRules() {
        return Rules.defaultRules();
    }

    @Override
    public MatchAuthorizer newAuthorizer() {
        return matchAuthorizer;
    }

    @Override
    public MatchMapModelProvider newMatchMapModelProvider() {
        return matchMapModelProvider;
    }
}
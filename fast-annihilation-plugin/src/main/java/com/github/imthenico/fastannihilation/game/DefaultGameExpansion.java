package com.github.imthenico.fastannihilation.game;

import com.github.imthenico.annihilation.api.game.GameExpansion;
import com.github.imthenico.annihilation.api.game.Options;
import com.github.imthenico.annihilation.api.game.Rules;
import com.github.imthenico.fastannihilation.match.expansion.DefaultMatchExpansion;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.fastannihilation.match.DefaultMatchAuthorizer;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.fastannihilation.strategy.DefaultMatchMapModelProvider;
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
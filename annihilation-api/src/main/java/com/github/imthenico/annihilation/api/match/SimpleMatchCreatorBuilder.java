package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.Annihilation;
import com.github.imthenico.annihilation.api.converter.ConverterToMatchMap;
import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.property.PropertyInterpreter;
import com.github.imthenico.annihilation.api.phase.DefaultPhaseExpansion;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.player.PlayerEventHandler;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.player.SimplePlayerEventHandler;
import com.github.imthenico.annihilation.api.player.SimplePlayerSetup;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SimpleMatchCreatorBuilder implements MatchFactory.Builder {

    private final Map<Class<?>, PropertyInterpreter<?, ?>> interpreterMap = new HashMap<>();
    private final Annihilation annihilation;
    private final String matchTypeName;

    private PhaseExpansion phaseExpansion;
    private Function<GameInstance, MatchClosingStage> endingProvider;
    private MatchEventHandler eventHandler;
    private PlayerSetup playerSetup;
    private PlayerEventHandler playerEventHandler;
    private ModelConverter<MatchMap> modelConverter;

    SimpleMatchCreatorBuilder(
            Annihilation annihilation,
            String matchTypeName
    ) {
        this.annihilation = annihilation;
        this.matchTypeName = matchTypeName;

        UtilityPack utilityPack = annihilation.utilities();
        MessageHandler messageHandler = utilityPack.getMessageHandler();

        this.endingProvider = (game) -> new DefaultMatchClosingStage(game.getRules().getTimeToEnd());
        this.eventHandler = new DefaultMatchEventHandler(messageHandler);
        this.phaseExpansion = new DefaultPhaseExpansion(messageHandler);
        this.playerSetup = new SimplePlayerSetup();
        this.playerEventHandler = new SimplePlayerEventHandler(messageHandler, annihilation.getScheduler());
    }

    @Override
    public MatchFactory.Builder setPhaseExpansion(PhaseExpansion expansion) {
        this.phaseExpansion = Validate.notNull(expansion);
        return this;
    }

    @Override
    public MatchFactory.Builder setEndingProvider(Function<GameInstance, MatchClosingStage> endingProvider) {
        this.endingProvider = Validate.notNull(endingProvider);
        return this;
    }

    @Override
    public MatchFactory.Builder setEventHandler(MatchEventHandler eventHandler) {
        this.eventHandler = Validate.notNull(eventHandler);
        return this;
    }

    @Override
    public MatchFactory.Builder setPlayerSetup(PlayerSetup playerSetup) {
        this.playerSetup = Validate.notNull(playerSetup);
        return this;
    }

    @Override
    public MatchFactory.Builder setPlayerEventHandler(PlayerEventHandler playerEventHandler) {
        this.playerEventHandler = Validate.notNull(playerEventHandler);
        return this;
    }

    @Override
    public MatchFactory.Builder setMatchMapProvider(ModelConverter<MatchMap> converter) {
        this.modelConverter = Validate.notNull(converter);
        return this;
    }

    @Override
    public <T> MatchFactory.Builder addMapPropertyInterpreter(Class<T> propertyValueType, PropertyInterpreter<T, ?> interpreter) {
        Validate.notNull(propertyValueType);

        if (interpreter == null) {
            this.interpreterMap.remove(propertyValueType);
            return this;
        }

        this.interpreterMap.put(propertyValueType, interpreter);
        return this;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public MatchFactory build() {
        if (modelConverter == null) {
            if (interpreterMap.isEmpty()) {
                throw new UnsupportedOperationException("No model to map converter provided");
            }

            modelConverter = new ConverterToMatchMap();

            interpreterMap.forEach((k, v) ->
                    modelConverter.addMapPropertyInterpreter((Class) k, (PropertyInterpreter) v));
        }

        return new DefaultMatchFactory(
                modelConverter,
                endingProvider,
                eventHandler,
                phaseExpansion,
                playerSetup,
                playerEventHandler,
                annihilation,
                matchTypeName
        );
    }
}
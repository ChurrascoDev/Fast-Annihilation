package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertyInterpreter;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.player.PlayerEventHandler;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.function.Function;

public interface MatchFactory {

    Match createMatch(GameInstance gameInstance, ConfigurableModel mapModel);

    String getProductTypeName();

    static MatchFactory.Builder builder(AnnihilationAPI annihilationAPI, String matchTypeName) {
        return new SimpleMatchCreatorBuilder(
                Validate.notNull(annihilationAPI, "annihilation"),
                Validate.notNull(matchTypeName, "matchTypeName")
        );
    }

    static MatchFactory create(AnnihilationAPI annihilationAPI, String matchTypeName) {
        return builder(annihilationAPI, matchTypeName).build();
    }

    interface Builder {

        Builder setPhaseExpansion(PhaseExpansion expansion);

        Builder setEndingProvider(Function<GameInstance, MatchClosingStage> endingProvider);

        Builder setEventHandler(MatchEventHandler eventHandler);

        Builder setPlayerSetup(PlayerSetup playerSetup);

        Builder setPlayerEventHandler(PlayerEventHandler playerEventHandler);

        <T> MatchFactory.Builder addMapPropertyInterpreter(
                Class<T> propertyValueType,
                PropertyInterpreter<T, ?> interpreter
        );

        Builder setMatchMapProvider(ModelConverter<MatchMap> converter);

        MatchFactory build();

    }
}
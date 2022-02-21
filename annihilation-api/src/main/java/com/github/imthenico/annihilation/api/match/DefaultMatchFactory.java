package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.converter.ModelConverter;
import com.github.imthenico.annihilation.api.event.MatchCreationEvent;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.phase.DefaultPhaseManager;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.player.PlayerEventHandler;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.MapPropertiesHelper;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;

import java.util.function.Function;

public class DefaultMatchFactory implements MatchFactory {

    private final ModelConverter<MatchMap> toMatchMapConverter;

    private final Function<GameInstance, MatchClosingStage> endingProvider;
    private final MatchEventHandler eventHandler;
    private final PhaseExpansion phaseExpansion;
    private final PlayerSetup playerSetup;
    private final PlayerEventHandler playerEventHandler;

    private final Scheduler scheduler;
    private final String matchTypeName;

    public DefaultMatchFactory(
            ModelConverter<MatchMap> toMatchMapConverter,
            Function<GameInstance, MatchClosingStage> endingProvider,
            MatchEventHandler eventHandler,
            PhaseExpansion expansion,
            PlayerSetup playerSetup,
            PlayerEventHandler playerEventHandler,
            AnnihilationAPI annihilationAPI,
            String matchTypeName
    ) {
        this.toMatchMapConverter = toMatchMapConverter;
        this.endingProvider = endingProvider;
        this.eventHandler = eventHandler;
        this.phaseExpansion = expansion;
        this.playerSetup = playerSetup;
        this.playerEventHandler = playerEventHandler;
        this.scheduler = annihilationAPI.getScheduler();
        this.matchTypeName = matchTypeName;
    }

    @Override
    public Match createMatch(GameInstance game, ConfigurableModel mapModel) {
        Validate.notNull(endingProvider);
        Validate.notNull(eventHandler);
        Validate.notNull(playerSetup);
        Validate.notNull(playerEventHandler);

        MatchMap matchMap = toMatchMapConverter.convert(mapModel, null);

        DefaultMatch match = new DefaultMatch(
                matchTypeName,
                matchMap,
                eventHandler,
                game,
                DefaultPhaseManager.createDefaultPhaseManager(
                        game,
                        (phase) -> MapPropertiesHelper.getPhaseDuration(phase, mapModel.getProperties()),
                        phaseExpansion.getPhaseActionFactory(),
                        phaseExpansion.supportedPhases()
                ),
                endingProvider.apply(game),
                playerSetup,
                playerEventHandler,
                scheduler
        );

        Bukkit.getPluginManager().callEvent(new MatchCreationEvent(match));

        return match;
    }

    @Override
    public String getProductTypeName() {
        return matchTypeName;
    }
}
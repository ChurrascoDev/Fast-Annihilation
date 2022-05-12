package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.event.match.MatchCreationEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.data.MatchMapData;
import com.github.imthenico.annihilation.api.phase.DefaultPhaseManager;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.inject.ConstructorModel;
import com.github.imthenico.inject.InjectionStructure;
import com.github.imthenico.inject.exception.InvocationException;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class DefaultMatchFactory implements MatchFactory {

    private final GameMapHandler modelDataHandler;
    private final Map<Class<?>, InjectionStructure> cachedStructures = new HashMap<>();
    private final ConstructorModel matchMapConstructorModel = ConstructorModel.fromClass(MatchMap.class);

    public DefaultMatchFactory(GameMapHandler modelDataHandler) {
        this.modelDataHandler = modelDataHandler;
    }

    @Override
    public Match createMatch(
            Game game, MatchExpansion gameExpansion, MapModel<? extends MatchMapData> mapModel
    ) {
        GameRoom room = game.room();
        String mapName = room.id() + "_" + mapModel.getName();

        InjectionStructure mapInjectionStructure = cachedStructures
                .computeIfAbsent(mapModel.getDataType(), dualKey -> AnnihilationAPI.INJECTION_HANDLER.createStructure(matchMapConstructorModel));

        MatchMap matchMap;
        try {
            matchMap = modelDataHandler.createMap(
                    mapModel,
                    mapInjectionStructure.getValues(mapModel.getData()),
                    mapName
            );
        } catch (InvocationException e) {
            throw new RuntimeException(e);
        }

        PhaseExpansion phaseExpansion = gameExpansion.getPhaseExpansion();

        MatchMap finalMatchMap = matchMap;
        PhaseManager phaseManager = DefaultPhaseManager.createDefaultPhaseManager(
                game,
                (phase) -> {
                    int phaseTime = finalMatchMap.getPhaseTime(phase);

                    if (phaseTime <= 0)
                        phaseTime = finalMatchMap.getTimePerPhase();

                    return phaseTime;
                },
                phaseExpansion.getPhaseActionFactory(),
                phaseExpansion.supportedPhases()
        );

        Match match = new DefaultMatch(
                matchMap,
                game,
                phaseManager,
                gameExpansion.getEndingProvider().apply(game),
                gameExpansion.getPlayerSetup()
        );

        Bukkit.getPluginManager().callEvent(new MatchCreationEvent(match));

        return match;
    }


}
package com.github.imthenico.fastannihilation.match;

import com.github.imthenico.annihilation.api.event.match.MatchCreationEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.fastannihilation.phase.DefaultPhaseManager;
import com.github.imthenico.annihilation.api.phase.PhaseExpansion;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.MapModel;
import org.bukkit.Bukkit;

public class DefaultMatchFactory implements MatchFactory {

    private final GameMapHandler modelDataHandler;
    public DefaultMatchFactory(GameMapHandler modelDataHandler) {
        this.modelDataHandler = modelDataHandler;
    }

    @Override
    public Match createMatch(
            Game game, MatchExpansion gameExpansion, MapModel<? extends MatchMapData> mapModel
    ) {
        GameRoom room = game.room();
        String mapName = room.id() + "_" + mapModel.getName();

        PhaseExpansion phaseExpansion = gameExpansion.getPhaseExpansion();

        MatchMap matchMap = modelDataHandler.createMap(
                mapModel,
                MatchMap.class,
                mapName
        );

        PhaseManager phaseManager = DefaultPhaseManager.createDefaultPhaseManager(
                game,
                (phase) -> {
                    int phaseTime = matchMap.getPhaseTime(phase);

                    if (phaseTime <= 0)
                        phaseTime = matchMap.getTimePerPhase();

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
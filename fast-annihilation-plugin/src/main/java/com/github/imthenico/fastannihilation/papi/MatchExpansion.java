package com.github.imthenico.fastannihilation.papi;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class MatchExpansion extends PlaceholderExpansion {

    private final PlayerRegistry playerRegistry;

    public MatchExpansion(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        AnniPlayer anniPlayer = playerRegistry.getPlayer(p.getUniqueId());
        if (anniPlayer == null) return null;

        MatchPlayer matchPlayer = MatchPlayer.from(anniPlayer);
        if (matchPlayer == null) return null;

        Match match = matchPlayer.getMatch();

        PhaseManager phaseManager = match.getPhaseManager();
        SimpleTimer timer = phaseManager.getTimer();
        PhaseManager.RunnablePhase currentPhase = phaseManager.getCurrentPhase();

        switch (params) {
            case "current_phase":
                if (currentPhase == null)
                    return "";

                return currentPhase.getPhaseNumber() + "";
            case "next_phase_time": {
                if (timer == null)
                    return "";

                return Formatting.formatSeconds(timer.getRemainingTime(), "%S:%S");
            }
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "match";
    }

    @Override
    public String getAuthor() {
        return "ImTheNico_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
package com.github.imthenico.fastannihilation.papi;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class AnniPlayerExpansion extends PlaceholderExpansion {

    private final PlayerRegistry playerRegistry;

    public AnniPlayerExpansion(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        AnniPlayer player = playerRegistry.getPlayer(p.getUniqueId());

        if (player == null)
            return null;

        if (params.equals("lobby")) {
            return "#1";
        }

        return null;
    }

    @Override
    public String getIdentifier() {
        return "anniplayer";
    }

    @Override
    public String getAuthor() {
        return "ImTheNico_";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
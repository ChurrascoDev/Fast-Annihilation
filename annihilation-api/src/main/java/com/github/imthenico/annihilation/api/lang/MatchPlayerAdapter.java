package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import me.yushust.message.resolve.EntityResolver;
import org.bukkit.entity.Player;

public class MatchPlayerAdapter implements EntityResolver<Player, MatchPlayer> {

    @Override
    public Player resolve(MatchPlayer matchPlayer) {
        return matchPlayer.getHandle().getPlayer();
    }
}
package com.github.imthenico.fastannihilation.lang;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import me.yushust.message.resolve.EntityResolver;
import org.bukkit.entity.Player;

public class AnniPlayerAdapter implements EntityResolver<Player, AnniPlayer> {

    @Override
    public Player resolve(AnniPlayer anniPlayer) {
        return anniPlayer.getPlayer();
    }
}
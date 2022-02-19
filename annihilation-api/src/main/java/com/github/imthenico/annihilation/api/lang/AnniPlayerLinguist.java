package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import me.yushust.message.bukkit.SpigotLinguist;
import me.yushust.message.language.Linguist;
import org.bukkit.entity.Player;

public class AnniPlayerLinguist implements Linguist<AnniPlayer> {

    private final Linguist<Player> playerLinguist;

    public AnniPlayerLinguist() {
        this.playerLinguist = new SpigotLinguist();
    }

    @Override
    public String getLanguage(AnniPlayer anniPlayer) {
        return anniPlayer.getLangOrDefault(playerLinguist.getLanguage(anniPlayer.getPlayer()));
    }
}
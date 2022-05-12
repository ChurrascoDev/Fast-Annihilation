package com.github.imthenico.fastannihilation.lang;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import me.yushust.message.bukkit.SpigotLinguist;
import me.yushust.message.language.Linguist;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerLinguist implements Linguist<Player> {

    private final PlayerRegistry playerRegistry;
    private final Linguist<Player> playerLinguist;

    public PlayerLinguist(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
        this.playerLinguist = new SpigotLinguist();
    }

    @Override
    public @Nullable String getLanguage(Player player) {
        AnniPlayer anniPlayer = playerRegistry.getPlayer(player.getUniqueId());

        if (anniPlayer != null) {
            return anniPlayer.getLangOrDefault();
        }

        return playerLinguist.getLanguage(player);
    }
}
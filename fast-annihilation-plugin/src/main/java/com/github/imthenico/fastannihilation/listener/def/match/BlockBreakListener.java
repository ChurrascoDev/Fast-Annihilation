package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.game.Options;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.MetadataValue;

public class BlockBreakListener implements Listener {

    private final PlayerRegistry playerRegistry;

    public BlockBreakListener(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        MatchPlayer matchPlayer = MatchPlayer.from(playerRegistry.getPlayer(player.getUniqueId()));

        if (matchPlayer == null)
            return;

        if (matchPlayer.isDisqualified())
            return;

        Block block = event.getBlock();

        if (!block.hasMetadata("nexus-reference"))
            return;

        MetadataValue metadataValue = block.getMetadata("nexus-reference")
                .stream()
                .filter(obj -> obj instanceof Nexus)
                .findFirst().orElse(null);

        if (metadataValue == null)
            return;

        Match match = matchPlayer.getMatch();
        Options options = match.getGame().getOptions();
        int damage = options.getNexusDamage() * options.getNexusDamageMultiplier();

        Nexus nexus = (Nexus) metadataValue.value();
        nexus.hit(damage, matchPlayer);
    }
}
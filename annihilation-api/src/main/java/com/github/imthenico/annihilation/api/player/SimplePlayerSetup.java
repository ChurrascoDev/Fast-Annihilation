package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.equipment.Equipment;
import com.github.imthenico.annihilation.api.equipment.Kit;
import com.github.imthenico.annihilation.api.match.Match;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class SimplePlayerSetup implements PlayerSetup {
    @Override
    public void setupPlayer(MatchPlayer matchPlayer, Match match) {
        Kit kit = matchPlayer.getKit();

        if (kit != null) {
            Player player = matchPlayer.getHandle().getPlayer();
            Equipment equipment = kit.getEquipment();

            equipment.getArmorPieces().forEach((k, v) -> k.set(player.getInventory(), v));

            equipment.getInventoryItems().asMap().forEach(player.getInventory()::setItem);

            for (PotionEffect effect : kit.getEffects()) {
                player.addPotionEffect(effect);
            }
        }
    }
}
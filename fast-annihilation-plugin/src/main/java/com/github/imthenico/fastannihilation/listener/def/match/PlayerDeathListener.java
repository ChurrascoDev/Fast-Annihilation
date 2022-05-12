package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import java.util.Objects;
import me.yushust.message.MessageHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final String expectedMatchTypeName;
    private final PlayerRegistry playerRegistry;
    private final MessageHandler messageHandler;
    private final Scheduler scheduler;

    public PlayerDeathListener(String expectedMatchTypeName, PlayerRegistry playerRegistry, MessageHandler messageHandler, Scheduler scheduler) {
        this.expectedMatchTypeName = Objects.requireNonNull(expectedMatchTypeName);
        this.playerRegistry = playerRegistry;
        this.messageHandler = messageHandler;
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        AnniPlayer anniPlayer = playerRegistry.getPlayer(player.getUniqueId());

        MatchPlayer matchPlayer = MatchPlayer.from(anniPlayer);

        if (matchPlayer == null)
            return;

        Match match = matchPlayer.getMatch();

        if (!match.getTypeName().equals(expectedMatchTypeName)) {
            return;
        }

        EntityDamageEvent damageEvent = player.getLastDamageCause();

        EntityDamageEvent.DamageCause damageCause = damageEvent.getCause();

        handleDeathEvent(
                matchPlayer,
                null,
                event,
                "player-death." + damageCause,
                "player-death-message",
                "<player>", getName(player)
        );
    }

    @EventHandler
    public void onDeathByEntity(PlayerDeathEvent event) {
        Player player = event.getEntity();
        AnniPlayer anniPlayer = playerRegistry.getPlayer(player.getUniqueId());

        MatchPlayer matchPlayer = MatchPlayer.from(anniPlayer);
        EntityDamageEvent lastDamageEvent = player.getLastDamageCause();
        EntityDamageByEntityEvent byEntityEvent = lastDamageEvent instanceof EntityDamageByEntityEvent ? (EntityDamageByEntityEvent) lastDamageEvent : null;

        if (matchPlayer == null || byEntityEvent == null)
            return;

        Match match = matchPlayer.getMatch();

        if (!match.getTypeName().equals(expectedMatchTypeName)) {
            return;
        }

        Entity damager = byEntityEvent.getEntity();

        String messagePath;
        String defaultMessagePath;

        if (damager instanceof Player) {
            messagePath = "player-killed-by-player";
            defaultMessagePath = messagePath;
        } else {
            messagePath = "player-killed-by-entity." + damager.getType().name();
            defaultMessagePath = "player-killed-by-entity-message";
        }

        handleDeathEvent(
                matchPlayer,
                damager,
                event,
                messagePath,
                defaultMessagePath,
                "<player>", getName(player),
                "<killer>", getName(damager)
        );
    }

    private void handleDeathEvent(
            MatchPlayer matchPlayer,
            Entity killer,
            PlayerDeathEvent event,
            String messagePath,
            String defaultMessagePath,
            Object... replacements
    ) {
        Player player = matchPlayer.getHandle().getPlayer();
        Match match = matchPlayer.getMatch();
        GameRoom game = match.getGame().room();

        // increment player death statistic

        if (killer instanceof Player) {
            // increment killer kills statistic
        }

        String path;
        for (AnniPlayer gamePlayer : game.getPlayers((p) -> true)) {
            String lang = messageHandler.getLanguage(gamePlayer);

            Object found = messageHandler.getSource().get(lang, messagePath);

            if (found == null) {
                path = defaultMessagePath;
            } else {
                path = messagePath;
            }

            messageHandler.sendReplacing(gamePlayer, path, replacements);
        }

        respawnPlayer(player);

        event.setDeathMessage(null);
    }

    private String getName(Entity entity) {
        String customName = entity.getCustomName();
        return customName != null ? customName : entity.getName();
    }

    private void respawnPlayer(Player player) {
        scheduler.scheduleSync(() -> player.spigot().respawn(), 1L);
    }
}
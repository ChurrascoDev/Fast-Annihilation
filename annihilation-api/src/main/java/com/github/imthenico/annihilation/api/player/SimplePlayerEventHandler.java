package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.PlayerDamageByEntityEvent;
import com.github.imthenico.annihilation.api.event.PlayerDamageEvent;
import com.github.imthenico.annihilation.api.event.PlayerJoinMatchEvent;
import com.github.imthenico.annihilation.api.event.PlayerLeaveMatchEvent;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.game.GameLobby;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class SimplePlayerEventHandler implements PlayerEventHandler {

    private final MessageHandler messageHandler;
    private final Scheduler scheduler;
    private final PluginManager pluginManager;

    public SimplePlayerEventHandler(
            MessageHandler messageHandler,
            Scheduler scheduler
    ) {
        this.messageHandler = messageHandler;
        this.scheduler = scheduler;
        this.pluginManager = Bukkit.getPluginManager();
    }

    @Override
    public void handleJoin(MatchPlayer matchPlayer) {
        Match match = matchPlayer.getMatch();

        GameInstance gameInstance = match.getGame();
        GameLobby lobby = gameInstance.getLobby();
        PreMatchStage preMatchStage = gameInstance.getPreparationStage();

        Map<UUID, TeamColor> teamSelection = preMatchStage.getTeamSelection();

        TeamColor color = teamSelection.get(matchPlayer.getUniqueId());

        if (color == null)
            return;

        MatchTeam team = match.getRunningMap().getTeam(color);

        team.join(matchPlayer);

        pluginManager.callEvent(new PlayerJoinMatchEvent(matchPlayer));
    }

    @Override
    public void handleLeave(MatchPlayer matchPlayer) {
        MatchTeam team = matchPlayer.getTeam();

        if (team != null && team.isDeath() && !matchPlayer.isDisqualified()) {
            matchPlayer.setDisqualified(true);
        }

        pluginManager.callEvent(new PlayerLeaveMatchEvent(matchPlayer));
    }

    @Override
    public void handleDeath(
            MatchPlayer matchPlayer,
            @NotNull PlayerDeathEvent event
    ) {
        Player player = event.getEntity();
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

    @Override
    public void handleDeathByEntity(
            MatchPlayer matchPlayer,
            @NotNull PlayerDeathEvent event,
            @NotNull PlayerDamageByEntityEvent lastDamageCause
    ) {
        Player player = event.getEntity();
        Entity damager = lastDamageCause.getDamager();

        String messagePath;
        String defaultMessagePath;

        if (lastDamageCause.playerDamager()) {
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

    @Override
    public void handleDamage(
            MatchPlayer matchplayer,
            @NotNull PlayerDamageEvent event
    ) {

    }

    @Override
    public void handleDamageByEntity(
            MatchPlayer matchplayer,
            @Nullable Entity damager,
            @Nullable MatchPlayer damagerAsMatchEntity,
            @NotNull PlayerDamageByEntityEvent event
    ) {

    }

    private String getName(Entity entity) {
        return Validate.defIfNull(entity.getCustomName(), entity.getName());
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
        GameInstance game = match.getGame();

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

    private void respawnPlayer(Player player) {
        scheduler.scheduleSync(() -> player.spigot().respawn(), 1L);
    }
}
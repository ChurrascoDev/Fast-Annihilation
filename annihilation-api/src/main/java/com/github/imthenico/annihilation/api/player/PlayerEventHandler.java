package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.PlayerDamageByEntityEvent;
import com.github.imthenico.annihilation.api.event.PlayerDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerEventHandler {

    void handleJoin(MatchPlayer matchPlayer);

    void handleLeave(MatchPlayer matchPlayer);

    void handleDeath(
            MatchPlayer matchPlayer,
            @NotNull PlayerDeathEvent event
    );

    void handleDeathByEntity(
            MatchPlayer matchPlayer,
            @NotNull PlayerDeathEvent event,
            @NotNull PlayerDamageByEntityEvent lastDamageCause
    );

    void handleDamage(
            MatchPlayer matchplayer,
            @NotNull PlayerDamageEvent event
    );

    void handleDamageByEntity(
            MatchPlayer matchplayer,
            @Nullable Entity damager,
            @Nullable MatchPlayer damagerAsMatchEntity,
            @NotNull PlayerDamageByEntityEvent event
    );

    default void handlePlayerRespawn(
            MatchPlayer matchPlayer,
            @NotNull PlayerRespawnEvent event
    ) {}
}
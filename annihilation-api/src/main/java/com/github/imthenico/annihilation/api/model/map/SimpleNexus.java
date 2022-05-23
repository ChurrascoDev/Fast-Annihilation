package com.github.imthenico.annihilation.api.model.map;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.NexusDamageEvent;
import com.github.imthenico.annihilation.api.event.match.NexusDestroyEvent;
import com.github.imthenico.annihilation.api.team.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SimpleNexus implements Nexus {

    private int health;
    private final TeamColor teamColor;
    private final MatchMap matchMap;
    private final Location location;

    public SimpleNexus(
            int health,
            TeamColor teamColor,
            Location location,
            MatchMap matchMap
    ) {
        if (health <= 0)
            throw new IllegalArgumentException("health <= 0");

        this.health = health;
        this.matchMap = Objects.requireNonNull(matchMap);
        this.teamColor = Objects.requireNonNull(teamColor);
        this.location = Objects.requireNonNull(location);
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public TeamColor color() {
        return teamColor;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void hit(int damage, MatchPlayer externalAgent) {
        if (externalAgent != null) {
            MatchMap playerMatchMap = externalAgent.getMatch().getRunningMap();

            if (!playerMatchMap.getMapName().equals(matchMap.getMapName())) {
                Player player = externalAgent.getPlayer();

                throw new IllegalArgumentException(player.getName() + " is not of the match " + matchMap.getMapName());
            }
        }

        health-=damage;

        NexusDamageEvent nexusEvent;

        if (health < 0) {
            health = 0;
            nexusEvent = new NexusDestroyEvent(matchMap, this, externalAgent, damage);
        } else {
            nexusEvent = new NexusDamageEvent(matchMap, this, externalAgent, damage);
        }

        Bukkit.getPluginManager().callEvent(nexusEvent);
    }
}
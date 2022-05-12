package com.github.imthenico.annihilation.api.team;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import org.bukkit.Location;

public interface MatchTeam extends Iterable<MatchPlayer> {

    TeamColor getColor();

    Nexus getNexus();

    Location getSpawn(int index);

    Location getRandomSpawn();

    boolean join(MatchPlayer matchPlayer);

    boolean leave(MatchPlayer matchPlayer);

    boolean isMember(MatchPlayer matchPlayer);

    default boolean isDeath() {
        boolean allPlayersDisqualified = true;

        for (MatchPlayer matchPlayer : this) {
            if (!matchPlayer.isDisqualified()) {
                allPlayersDisqualified = false;
                break;
            }
        }

        return allPlayersDisqualified && getNexus().getHealth() <= 0;
    }
}
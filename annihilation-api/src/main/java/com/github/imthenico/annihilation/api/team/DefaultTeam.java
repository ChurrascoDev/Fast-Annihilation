package com.github.imthenico.annihilation.api.team;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.Nexus;
import com.github.imthenico.annihilation.api.util.RandomElementPicker;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DefaultTeam implements MatchTeam {

    private final TeamColor color;
    private final List<MatchPlayer> members;
    private final MatchMap matchMap;

    public DefaultTeam(
            TeamColor color,
            MatchMap matchMap
    ) {
        this.color = Objects.requireNonNull(color);
        this.matchMap = Objects.requireNonNull(matchMap);
        this.members = new ArrayList<>();
    }

    @Override
    public TeamColor getColor() {
        return color;
    }

    @Override
    public Nexus getNexus() {
        return matchMap.getTeamNexus(color);
    }

    @Override
    public Location getSpawn(int index) {
        List<Location> spawns = matchMap.getTeamSpawns(color);

        if (index >= spawns.size())
            return null;

        return spawns.get(index);
    }

    @Override
    public Location getRandomSpawn() {
        List<Location> spawns = matchMap.getTeamSpawns(color);

        return RandomElementPicker.pickRandom(spawns);
    }

    @Override
    public boolean join(MatchPlayer matchPlayer) {
        otherTeamsMemberCheck(matchPlayer);

        boolean success = false;
        if (!members.contains(matchPlayer)) {
            success = true;
            members.add(matchPlayer);
        }

        matchPlayer.handleTeamJoin(this);
        return success;
    }

    @Override
    public boolean leave(MatchPlayer matchPlayer) {
        boolean success = false;
        if (members.contains(matchPlayer)) {
            success = true;
            members.remove(matchPlayer);
        }

        matchPlayer.handleTeamLeave();
        return success;
    }

    @Override
    public boolean isMember(MatchPlayer matchPlayer) {
        return members.contains(matchPlayer);
    }

    @Override
    public Iterator<MatchPlayer> iterator() {
        Iterator<MatchPlayer> iterator = members.iterator();

        return new Iterator<MatchPlayer>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public MatchPlayer next() {
                return iterator.next();
            }
        };
    }

    private void otherTeamsMemberCheck(MatchPlayer matchPlayer) {
        for (TeamColor color : TeamColor.values()) {
            if (color == this.color)
                continue;

            MatchTeam gameTeam = matchMap.getTeam(color);

            if (gameTeam.isMember(matchPlayer))
                throw new IllegalArgumentException("That player is already member of other team.");
        }
    }
}
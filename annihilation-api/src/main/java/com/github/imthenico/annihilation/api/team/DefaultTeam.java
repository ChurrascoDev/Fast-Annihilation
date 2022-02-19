package com.github.imthenico.annihilation.api.team;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.ingame.Nexus;
import com.github.imthenico.annihilation.api.map.MapContext;
import com.github.imthenico.annihilation.api.property.PropertyKeys;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.simplecommons.iterator.UnmodifiableIterator;
import com.github.imthenico.simplecommons.util.Validate;
import com.github.imthenico.simplecommons.util.list.CustomList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DefaultTeam implements MatchTeam {

    private final TeamColor color;
    private final List<MatchPlayer> members;
    private final MapContext mapContext;

    public DefaultTeam(
            TeamColor color,
            MapContext mapContext
    ) {
        this.color = Validate.notNull(color);
        this.mapContext = Validate.notNull(mapContext);
        this.members = new ArrayList<>();
    }

    @Override
    public TeamColor getColor() {
        return color;
    }

    @Override
    public Nexus getNexus() {
        return mapContext.getProperty(PropertyKeys.teamNexus(color)).orDefault(null);
    }

    @Override
    public LocationReference getSpawn(int index) {
        CustomList<LocationReference> spawns = mapContext.getMapProperties(PropertyKeys.teamSpawns(color)).orDefault(null);

        return spawns.safeGet(index).orElse(null);
    }

    @Override
    public LocationReference getRandomSpawn() {
        CustomList<LocationReference> spawns = mapContext.getMapProperties(PropertyKeys.teamSpawns(color)).orDefault(null);

        return spawns.random().orElse(null);
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

        matchPlayer.handleTeamLeave(this);
        return success;
    }

    @Override
    public boolean isMember(MatchPlayer matchPlayer) {
        return members.contains(matchPlayer);
    }

    @Override
    public Iterator<MatchPlayer> iterator() {
        return new UnmodifiableIterator<>(members.iterator());
    }

    private void otherTeamsMemberCheck(MatchPlayer matchPlayer) {
        for (TeamColor color : TeamColor.values()) {
            if (color == this.color)
                continue;

            MatchTeam gameTeam = mapContext.getTeam(color);

            Validate.isTrue(!gameTeam.isMember(matchPlayer), "That player is already member of other team.");
        }
    }
}
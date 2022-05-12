package com.github.imthenico.annihilation.api.model.map;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import org.bukkit.Location;

public interface Nexus {

    int getHealth();

    Location getLocation();

    void hit(int damage, MatchPlayer externalAgent);

    TeamColor color();

}
package com.github.imthenico.annihilation.api.ingame;

import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.world.LocationReference;

public interface Nexus {

    int getHealth();

    LocationReference getLocation();

    void hit(int damage);

    TeamColor color();

}
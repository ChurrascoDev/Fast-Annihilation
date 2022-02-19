package com.github.imthenico.annihilation.api.ingame;

import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.util.Validate;

public class SimpleNexus implements Nexus {

    private int health;
    private final TeamColor teamColor;
    private final LocationReference locationReference;

    public SimpleNexus(
            int health,
            TeamColor teamColor,
            LocationReference locationReference
    ) {
        this.health = Validate.isTrue(health > 0, health);
        this.teamColor = Validate.notNull(teamColor);
        this.locationReference = Validate.notNull(locationReference);
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
    @SuppressWarnings("unchecked")
    public LocationReference getLocation() {
        return locationReference;
    }

    public void hit(int damage) {
        health-=damage;

        if (health < 0)
            health = 0;
    }
}
package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.team.TeamColor;

import static com.github.imthenico.annihilation.api.property.PropertyKey.of;
import static com.github.imthenico.annihilation.api.property.PropertyKey.withNestedTeam;

public interface PropertyKeys {

    static PropertyKey teamNexus(TeamColor color) {
        return withNestedTeam("nexus", color, false);
    }

    static PropertyKey teamSpawns(TeamColor color) {
        return withNestedTeam("spawns", color, false);
    }

    static PropertyKey teamSpectatorPositions(TeamColor color) {
        return withNestedTeam("spectator_positions", color, false);
    }

    static PropertyKey furnaces() {
        return of("furnace_positions", false);
    }

    static PropertyKey specialBlocks() {
        return of("regenerable_blocks", false);
    }

    static PropertyKey bosses() {
        return of("boss_list", false);
    }

    static PropertyKey timePerPhase() {
        return of("default_time_per_phase", false);
    }

    static PropertyKey phaseTime(int phase) {
        return of(String.format("phase_%s_time", phase), false);
    }
}
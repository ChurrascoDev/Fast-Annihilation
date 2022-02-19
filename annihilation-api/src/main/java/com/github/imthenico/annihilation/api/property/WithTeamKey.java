package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.team.TeamColor;

import java.util.Objects;

public class WithTeamKey extends SimplePropertyKey {

    private final TeamColor color;

    public WithTeamKey(String name, boolean overWritable, TeamColor color) {
        super(name, overWritable);
        this.color = color;
    }

    public TeamColor getTeamColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WithTeamKey that = (WithTeamKey) o;
        return color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color);
    }
}
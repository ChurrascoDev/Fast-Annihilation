package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.simplecommons.util.Validate;

public interface PropertyKey {

    String name();

    boolean isOverWritable();

    static PropertyKey of(String keyName) {
        return of(keyName, true);
    }

    static PropertyKey of(String keyName, boolean overWritable) {
        return new SimplePropertyKey(keyName, overWritable);
    }

    static WithTeamKey withNestedTeam(String keyName, TeamColor color, boolean overWritable) {
        return new WithTeamKey(Validate.notNull(keyName), overWritable, Validate.notNull(color));
    }
}
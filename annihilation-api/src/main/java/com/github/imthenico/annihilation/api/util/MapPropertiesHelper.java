package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.map.model.NexusModel;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKeys;
import com.github.imthenico.annihilation.api.team.TeamColor;

public interface MapPropertiesHelper {

    static NexusModel getNexusModel(TeamColor color, PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.teamNexus(color);

        return propertiesContainer.getProperty(key).get();
    }

    static Integer getPhaseDuration(int phase, PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.phaseTime(phase);

        return propertiesContainer.getProperty(key).get();
    }

    static boolean setNexusModel(TeamColor color, NexusModel model, PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.teamNexus(color);

        return propertiesContainer.set(key, model);
    }

    static boolean setPhaseDuration(int phase, int duration, PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.phaseTime(phase);

        return propertiesContainer.set(key, duration);
    }

    static boolean setTimePerPhase(int time, PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.timePerPhase();

        return propertiesContainer.set(key, time <= 0 ? 600 : time);
    }

    static int getTimePerPhase(PropertiesContainer propertiesContainer) {
        PropertyKey key = PropertyKeys.timePerPhase();

        return propertiesContainer.getProperty(key).orDefault(() -> 300);
    }
}
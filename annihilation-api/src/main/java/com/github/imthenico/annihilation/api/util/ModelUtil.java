package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyHolder;
import com.github.imthenico.annihilation.api.property.PropertyKey;

import java.util.Arrays;

public interface ModelUtil {

    static boolean hasTag(PropertiesContainer propertiesContainer, String... tags) {
        return propertiesContainer.getList(PropertyKey.of("tags")).containsAll(Arrays.asList(tags));
    }

    static boolean hasTag(PropertyHolder propertyHolder, String... tags) {
        return hasTag(propertyHolder.getProperties(), tags);
    }
}
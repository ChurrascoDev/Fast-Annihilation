package com.github.imthenico.annihilation.api.property;

import java.util.Objects;

public class SimplePropertyKey implements PropertyKey {

    private final String name;
    private final boolean overWritable;

    public SimplePropertyKey(
            String name,
            boolean overWritable
    ) {
        this.name = name;
        this.overWritable = overWritable;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isOverWritable() {
        return overWritable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePropertyKey that = (SimplePropertyKey) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
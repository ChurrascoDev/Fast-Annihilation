package com.github.imthenico.annihilation.api.entity;

import com.github.imthenico.simplecommons.util.Validate;

import java.util.Objects;

public class StringEntityId {

    private final String id;

    public StringEntityId(String id) {
        this.id = Validate.notNull(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringEntityId that = (StringEntityId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
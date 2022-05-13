package com.github.imthenico.annihilation.api.namespace;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Namespace {

    private final String str;
    private final List<String> parts;

    Namespace(String str, String[] parts) {
        this.str = str;
        this.parts = Arrays.asList(parts);
    }

    public String get() {
        return str;
    }

    public List<String> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public boolean identify(String text) {
        return str.equals(text);
    }

    public boolean has(String str) {
        return parts.contains(str);
    }

    public Namespace ifIsIdentityExecute(String text, Runnable runnable) {
        if (identify(text)) runnable.run();
        return this;
    }

    public Namespace ifHasExecute(String text, Runnable runnable) {
        if (has(text)) runnable.run();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace namespace = (Namespace) o;
        return str.equals(namespace.str) && parts.equals(namespace.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(str, parts);
    }

    public static Namespace of(String text) {
        String[] separated = text.split("_");

        if (separated.length < 2)
            throw new IllegalArgumentException("Invalid namespace");

        return new Namespace(text, separated);
    }
}
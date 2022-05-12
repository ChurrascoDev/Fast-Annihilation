package com.github.imthenico.annihilation.api.lang;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface LangHolder {

    String DEFAULT_LANG = "en";

    @Nullable String getLang();

    default String getLangOrDefault(String def) {
        String lang = getLang();

        if (lang != null)
            return lang;

        return def;
    }

    default String getLangOrDefault() {
        return getLangOrDefault(DEFAULT_LANG);
    }
}
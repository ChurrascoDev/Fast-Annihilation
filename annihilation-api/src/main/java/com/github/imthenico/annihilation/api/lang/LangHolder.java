package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.simplecommons.util.Validate;
import org.jetbrains.annotations.Nullable;

public interface LangHolder {

    String DEFAULT_LANG = "english";

    @Nullable String getLang();

    default String getLangOrDefault(String def) {
        return Validate.defIfNull(getLang(), def);
    }

    default String getLangOrDefault() {
        return getLangOrDefault(DEFAULT_LANG);
    }
}
package com.github.imthenico.annihilation.api.data;

import com.github.imthenico.annihilation.api.util.SafeCast;

public interface DataHolder {

    Object putValue(String key, Object value);

    Object removeValue(String key);

    SafeCast<?> getValue(String key);

    boolean has(String key);

}
package com.github.imthenico.annihilation.api.util;

import java.util.Arrays;

public class BasicTypes {

    private final static Class<?>[] BASIC_TYPES;

    static {
        BASIC_TYPES = new Class[] {
                String.class,
                int.class,
                byte.class,
                short.class,
                double.class,
                long.class,
                float.class,
                boolean.class,
                char.class
        };
    }

    public static Class<?>[] getBasicTypes() {
        return Arrays.copyOf(BASIC_TYPES, BASIC_TYPES.length);
    }

    public static boolean isBasic(Object obj) {
        for (Class<?> basicType : BASIC_TYPES) {
            if (basicType.isInstance(obj))
                return true;
        }

        return false;
    }
}
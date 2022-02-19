package com.github.imthenico.fastannihilation.mapping;

import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.mapper.gson.GsonMapper;
import com.github.imthenico.simplecommons.util.Validate;
import com.google.gson.GsonBuilder;

public class DefaultMapperInstance {

    private static GenericMapper<String> GSON_MAPPER = new GsonMapper(new GsonBuilder().serializeNulls().setPrettyPrinting());

    public static void overWrite(GenericMapper<String> mapper) {
        GSON_MAPPER = Validate.notNull(mapper, "mapper cannot be null");
    }

    public static GenericMapper<String> getMapper() {
        return GSON_MAPPER;
    }
}
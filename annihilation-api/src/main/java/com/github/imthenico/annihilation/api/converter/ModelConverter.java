package com.github.imthenico.annihilation.api.converter;

import com.github.imthenico.annihilation.api.exception.ConverterParseException;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertyInterpreter;

import java.util.Map;

public interface ModelConverter<T> {

    T convert(ConfigurableModel model, Map<String, Object> extraData) throws ConverterParseException;

    <S> ModelConverter<T> addMapPropertyInterpreter(
            Class<S> propertyValueType,
            PropertyInterpreter<S, ?> interpreter
    );
}
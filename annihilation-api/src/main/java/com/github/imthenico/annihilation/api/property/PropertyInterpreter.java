package com.github.imthenico.annihilation.api.property;

public interface PropertyInterpreter<T, O> {

    O readProperty(PropertyKey propertyKey, T property);

}
package com.github.imthenico.annihilation.api.util;

public interface ThrowableSupplier<T> {

    T get() throws Exception;

}
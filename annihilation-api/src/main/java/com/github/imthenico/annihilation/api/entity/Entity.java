package com.github.imthenico.annihilation.api.entity;

public interface Entity<T> {

    <S extends T> S getHandle();

    default void remove() {}

}
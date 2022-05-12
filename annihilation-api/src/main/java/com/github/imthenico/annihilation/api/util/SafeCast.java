package com.github.imthenico.annihilation.api.util;

import com.google.gson.reflect.TypeToken;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SafeCast<T> {

    private final T source;

    public SafeCast(T source) {
        this.source = source;
    }

    public T getSource() {
        return source;
    }

    @SuppressWarnings("unchecked")
    public <S> S get() {
        return (S) source;
    }

    @SuppressWarnings("unchecked")
    public <S> S get(TypeToken<S> token) {
        if (!token.getRawType().isInstance(source))
            throw new ClassCastException();

        return (S) source;
    }

    @SuppressWarnings("unchecked")
    public <S> S orThrow(ThrowableSupplier<Throwable> throwableSupplier) {
        try {
            return (S) source;
        } catch (ClassCastException e) {
            try {
                throw throwableSupplier.get();
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<S> tryCast(Class<S> target) {
        if (!target.isInstance(source))
            return Optional.ofNullable((S) getSource());

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<S> uncheckedCast() {
        try {
            return Optional.ofNullable((S) source);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public <S> Optional<S> checkedCast(Predicate<Object> predicate) {
        return predicate.test(source) ? Optional.ofNullable((S) source) : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <S> S orDefault(Supplier<S> supplier) {
        try {
            if (source == null)
                return supplier.get();

            return (S) source;
        } catch (ClassCastException e) {
            return supplier.get();
        }
    }

    public static <T> SafeCast<T> of(T source) {
        return new SafeCast<>(source);
    }
}
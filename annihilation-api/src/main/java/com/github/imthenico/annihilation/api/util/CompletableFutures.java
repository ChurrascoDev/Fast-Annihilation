package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.exception.TimeoutException;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CompletableFutures {

    ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    static <T> CompletableFuture<T> supplyAsync(
            Supplier<T> supplier,
            Duration duration,
            String timedOutMsg
    ) {
        CompletableFuture<T> future = supplyAsync(supplier);

        EXECUTOR_SERVICE.schedule(() -> {
            if (!future.isDone()) {
                future.completeExceptionally(new TimeoutException(timedOutMsg, duration));
            }
        }, duration.toMillis(), TimeUnit.MILLISECONDS);

        return future;
    }

    static <T> CompletableFuture<T> supplyAsync(
            Supplier<T> supplier,
            Duration duration
    ) {
        return supplyAsync(supplier, duration, String.format("Timed out after %sms", duration.toMillis()));
    }

    static CompletableFuture<Void> runAsync(
            Runnable runnable,
            Duration duration,
            String timedOutMsg
    ) {
        CompletableFuture<Void> future = runAsync(runnable);

        EXECUTOR_SERVICE.schedule(() -> {
            if (!future.isDone()) {
                future.completeExceptionally(new TimeoutException(timedOutMsg, duration));
            }
        }, duration.toMillis(), TimeUnit.MILLISECONDS);

        return future;
    }

    static CompletableFuture<Void> runAsync(
            Runnable runnable,
            Duration duration
    ) {
        return runAsync(runnable, duration, String.format("Timed out after %sms", duration.toMillis()));
    }

    static <T> CompletableFuture<T> timed(
            CompletableFuture<T> future,
            Duration duration,
            String timedOutMsg
    ) {
        EXECUTOR_SERVICE.schedule(() -> {
            if (!future.isDone()) {
                future.completeExceptionally(new TimeoutException(timedOutMsg, duration));
            }
        }, duration.toMillis(), TimeUnit.MILLISECONDS);

        return future;
    }

    static <T> CompletableFuture<T> timed(
            CompletableFuture<T> future,
            Duration duration,
            Consumer<CompletableFuture<T>> onTimeOut
    ) {
        EXECUTOR_SERVICE.schedule(() -> {
            if (!future.isDone()) {
                onTimeOut.accept(future);
                future.completeExceptionally(new TimeoutException(duration));
            }
        }, duration.toMillis(), TimeUnit.MILLISECONDS);

        return future;
    }

    static <T> CompletableFuture<T> timed(
            CompletableFuture<T> future,
            Duration duration
    ) {
        return timed(future, duration, String.format("Timed out after %sms", duration.toMillis()));
    }

    static CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }

    static <T> CompletableFuture<T> withErrorPrinter() {
        return new CompletableFuture<T>().exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
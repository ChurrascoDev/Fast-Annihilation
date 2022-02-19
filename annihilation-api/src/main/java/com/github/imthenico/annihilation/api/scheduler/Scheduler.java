package com.github.imthenico.annihilation.api.scheduler;

import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Scheduler {

    BukkitTask executeConstantly(Runnable runnable, long delay, long interval);

    BukkitTask executeConstantly(Runnable runnable, long interval);

    BukkitTask executeConstantlyAsync(Runnable runnable, long delay, long interval);

    BukkitTask executeConstantlyAsync(Runnable runnable, long interval);

    int scheduleSync(Runnable runnable, long delay);

    int scheduleAsync(Runnable runnable, long delay);

    <T> CompletableFuture<T> supply(Supplier<T> supplier, boolean async);

    <T> CompletableFuture<T> call(Callable<T> callable, boolean async);

    CompletableFuture<Integer> run(Runnable runnable, boolean async);

}
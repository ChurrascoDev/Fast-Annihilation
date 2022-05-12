package com.github.imthenico.fastannihilation.scheduler;

import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.CompletableFutures;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SimpleBukkitScheduler implements Scheduler {

    private final Plugin plugin;
    private final BukkitScheduler bukkitScheduler;

    public SimpleBukkitScheduler(Plugin plugin) {
        this.plugin = Objects.requireNonNull(plugin);
        this.bukkitScheduler = Bukkit.getScheduler();
    }

    @Override
    public BukkitTask executeConstantly(Runnable runnable, long delay, long interval) {
        return bukkitScheduler.runTaskTimer(plugin, runnable, delay, interval);
    }

    @Override
    public BukkitTask executeConstantly(Runnable runnable, long interval) {
        return executeConstantly(runnable, 0, interval);
    }

    @Override
    public BukkitTask executeConstantlyAsync(Runnable runnable, long delay, long interval) {
        return bukkitScheduler.runTaskTimerAsynchronously(plugin, runnable, delay, interval);
    }

    @Override
    public BukkitTask executeConstantlyAsync(Runnable runnable, long interval) {
        return executeConstantlyAsync(runnable, 0, interval);
    }

    @Override
    public int scheduleSync(Runnable runnable, long delay) {
        return bukkitScheduler.scheduleSyncDelayedTask(plugin, runnable, delay);
    }

    @Override
    public int scheduleAsync(Runnable runnable, long delay) {
        return bukkitScheduler.scheduleAsyncDelayedTask(plugin, runnable, delay);
    }

    @Override
    public <T> CompletableFuture<T> supply(Supplier<T> supplier, boolean async) {
        CompletableFuture<T> completableFuture = CompletableFutures.withErrorPrinter();

        Runnable completion = () -> completableFuture.complete(supplier.get());

        if (!async) {
            scheduleSync(completion, 0);
        } else {
            scheduleAsync(completion, 0);
        }

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> call(Callable<T> callable, boolean async) {
        return supply(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }, async);
    }

    @Override
    public CompletableFuture<Integer> run(Runnable runnable, boolean async) {
        CompletableFuture<Integer> completableFuture = CompletableFutures.withErrorPrinter();

        if (!async) {
            scheduleSync(runnable, 0);
        } else {
            scheduleAsync(runnable, 0);
        }

        return completableFuture;
    }
}
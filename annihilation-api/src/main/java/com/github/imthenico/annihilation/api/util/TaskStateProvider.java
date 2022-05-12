package com.github.imthenico.annihilation.api.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public interface TaskStateProvider {

    static boolean isActive(int taskId) {
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

        return bukkitScheduler.isCurrentlyRunning(taskId) || bukkitScheduler.isQueued(taskId);
    }

    static boolean isActive(BukkitRunnable bukkitRunnable) {
        try {
            return isActive(bukkitRunnable.getTaskId());
        } catch (IllegalStateException e) {
            return false;
        }
    }

    static boolean isRunning(int taskId) {
        BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

        return bukkitScheduler.isCurrentlyRunning(taskId);
    }

    static boolean isRunning(BukkitRunnable bukkitRunnable) {
        try {
            return isRunning(bukkitRunnable.getTaskId());
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
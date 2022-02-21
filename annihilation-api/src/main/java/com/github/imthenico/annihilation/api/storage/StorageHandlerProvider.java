package com.github.imthenico.annihilation.api.storage;

import com.github.imthenico.annihilation.api.config.AnniConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface StorageHandlerProvider {

    Executor STORAGE_EXECUTOR = Executors.newFixedThreadPool(3);

    AnniStorage createHandler(JavaPlugin plugin, AnniConfig anniConfig) throws Exception;

}
package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.fastannihilation.config.AnniConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface StorageHandlerProvider {

    Executor STORAGE_EXECUTOR = Executors.newFixedThreadPool(3);

    AnniStorage createHandler(JavaPlugin plugin, AnniConfig anniConfig) throws Exception;

}
package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.annihilation.api.config.AnniConfig;
import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.fastannihilation.storage.StorageHandlerProvider;
import com.github.imthenico.annihilation.api.storage.StorageSourceType;
import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.fastannihilation.repository.impl.DirectoryRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FileStorageHandlerProvider implements StorageHandlerProvider {

    private final Gson gson;

    public FileStorageHandlerProvider(Gson gson) {
        this.gson = gson;
    }

    @Override
    public AnniStorage createHandler(
            JavaPlugin plugin, AnniConfig anniConfig
    ) throws Exception {
        File file = new File(plugin.getDataFolder(), "annihilation-data");

        StorageSource storageSource = new StorageSource(file, StorageSourceType.FILE);
        AbstractRepository<JsonObject> mapModelDataRepository = createRepository(
                new File(file, "models"), JsonObject.class
        );

        return new SimpleAnniStorage(
                mapModelDataRepository,
                storageSource
        );
    }

    private <T> AbstractRepository<T> createRepository(
            File folder,
            Class<T> modelClass
    ) {
        return new DirectoryRepository<>(
                STORAGE_EXECUTOR,
                gson,
                modelClass,
                folder
        );
    }
}
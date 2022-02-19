package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.fastannihilation.config.AnniConfig;
import com.github.imthenico.fastannihilation.mapping.DefaultMapperInstance;
import com.github.imthenico.fastannihilation.storage.AnniStorageHandler;
import com.github.imthenico.fastannihilation.storage.StorageHandlerProvider;
import com.github.imthenico.fastannihilation.storage.StorageSource;
import com.github.imthenico.fastannihilation.storage.StorageSourceType;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;
import com.github.imthenico.simplecommons.data.repository.FileRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FileStorageHandlerProvider implements StorageHandlerProvider {

    @Override
    public AnniStorageHandler createHandler(
            JavaPlugin plugin, AnniConfig anniConfig
    ) throws Exception {
        File file = new File(plugin.getDataFolder(), "annihilation-map-data");

        StorageSource storageSource = new StorageSource(StorageSourceType.FILE, file);
        AbstractRepository<TreeNode> mapModelDataRepository = createRepository(
                file, TreeNode.class
        );

        return new SimpleAnniStorageHandler(
                mapModelDataRepository,
                storageSource
        );
    }

    private <T> AbstractRepository<T> createRepository(
            File folder,
            Class<T> modelClass
    ) {
        return new FileRepository<>(
                STORAGE_EXECUTOR,
                modelClass,
                DefaultMapperInstance.getMapper(),
                folder,
                ".yml"
        );
    }
}
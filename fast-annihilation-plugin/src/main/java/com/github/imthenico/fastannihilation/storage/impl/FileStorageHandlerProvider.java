package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.fastannihilation.config.AnniConfig;
import com.github.imthenico.fastannihilation.mapping.DefaultMapperInstance;
import com.github.imthenico.fastannihilation.storage.AnniStorage;
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
    public AnniStorage createHandler(
            JavaPlugin plugin, AnniConfig anniConfig
    ) throws Exception {
        File file = new File(plugin.getDataFolder(), "annihilation-model-data");

        StorageSource storageSource = new StorageSource(StorageSourceType.FILE, file);
        AbstractRepository<TreeNode> mapModelDataRepository = createRepository(
                file, TreeNode.class
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
        return new FileRepository<>(
                STORAGE_EXECUTOR,
                modelClass,
                DefaultMapperInstance.getMapper(),
                folder,
                ".yml"
        );
    }
}
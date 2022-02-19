package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.fastannihilation.storage.AnniStorage;
import com.github.imthenico.fastannihilation.storage.StorageSource;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

public class SimpleAnniStorage implements AnniStorage {

    private final AbstractRepository<TreeNode> mapModelDataRepository;
    private final StorageSource storageSource;

    public SimpleAnniStorage(
            AbstractRepository<TreeNode> mapModelDataRepository,
            StorageSource storageSource
    ) {
        this.mapModelDataRepository = mapModelDataRepository;
        this.storageSource = storageSource;
    }

    @Override
    public AbstractRepository<TreeNode> getModelDataRepository() {
        return mapModelDataRepository;
    }

    @Override
    public StorageSource getStorageSource() {
        return storageSource;
    }
}
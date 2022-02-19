package com.github.imthenico.fastannihilation.storage.impl;

import com.github.imthenico.fastannihilation.storage.AnniStorageHandler;
import com.github.imthenico.fastannihilation.storage.StorageSource;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

public class SimpleAnniStorageHandler implements AnniStorageHandler {

    private final AbstractRepository<TreeNode> mapModelDataRepository;
    private final StorageSource storageSource;

    public SimpleAnniStorageHandler(
            AbstractRepository<TreeNode> mapModelDataRepository,
            StorageSource storageSource
    ) {
        this.mapModelDataRepository = mapModelDataRepository;
        this.storageSource = storageSource;
    }

    @Override
    public AbstractRepository<TreeNode> getMapModelDataRepository() {
        return mapModelDataRepository;
    }

    @Override
    public StorageSource getStorageSource() {
        return storageSource;
    }
}
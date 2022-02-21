package com.github.imthenico.annihilation.api.storage;

import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

public interface AnniStorage {

    AbstractRepository<TreeNode> getModelDataRepository();

    StorageSource getStorageSource();

}
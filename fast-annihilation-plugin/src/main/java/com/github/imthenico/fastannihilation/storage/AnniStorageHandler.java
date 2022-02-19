package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

public interface AnniStorageHandler {

    AbstractRepository<TreeNode> getMapModelDataRepository();

    StorageSource getStorageSource();

}
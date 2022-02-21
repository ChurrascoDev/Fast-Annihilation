package com.github.imthenico.fastannihilation.delegate;

import com.github.imthenico.annihilation.api.storage.AnniStorage;
import com.github.imthenico.annihilation.api.storage.StorageSource;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

import java.util.function.Supplier;

public interface DelegatingFactory {

    static AnniStorage newStorageHandler(
            Supplier<AnniStorage> delegate
    ) {
        return new AnniStorage() {
            @Override
            public AbstractRepository<TreeNode> getModelDataRepository() {
                return delegate.get().getModelDataRepository();
            }

            @Override
            public StorageSource getStorageSource() {
                return delegate.get().getStorageSource();
            }
        };
    }
}
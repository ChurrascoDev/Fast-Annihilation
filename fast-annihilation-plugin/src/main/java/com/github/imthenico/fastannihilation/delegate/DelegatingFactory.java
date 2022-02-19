package com.github.imthenico.fastannihilation.delegate;

import com.github.imthenico.fastannihilation.storage.AnniStorageHandler;
import com.github.imthenico.fastannihilation.storage.StorageSource;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

import java.util.function.Supplier;

public interface DelegatingFactory {

    static AnniStorageHandler newStorageHandler(
            Supplier<AnniStorageHandler> delegate
    ) {
        return new AnniStorageHandler() {
            @Override
            public AbstractRepository<TreeNode> getMapModelDataRepository() {
                return delegate.get().getMapModelDataRepository();
            }

            @Override
            public StorageSource getStorageSource() {
                return delegate.get().getStorageSource();
            }
        };
    }
}
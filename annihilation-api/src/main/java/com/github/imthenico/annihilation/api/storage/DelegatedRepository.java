package com.github.imthenico.annihilation.api.storage;

import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class DelegatedRepository<T> extends AbstractRepository<T> {

    private final AtomicReference<AbstractRepository<T>> delegate;

    public DelegatedRepository(
            AtomicReference<AbstractRepository<T>> delegate
    ) {
        super(null, null);
        this.delegate = delegate;
    }

    @Override
    public int delete(SourceKey sourceKey) {
        return getRepository().delete(sourceKey);
    }

    @Override
    public T usingId(SourceKey sourceKey) {
        return getRepository().usingId(sourceKey);
    }

    @Override
    public Set<T> all() {
        return getRepository().all();
    }

    @Override
    public Set<SourceKey> keys() {
        return getRepository().keys();
    }

    @Override
    public void save(T t, SourceKey sourceKey) {
        getRepository().save(t, sourceKey);
    }

    @Override
    public boolean asyncMode() {
        return getRepository().asyncMode();
    }

    private AbstractRepository<T> getRepository() {
        return delegate.get();
    }
}
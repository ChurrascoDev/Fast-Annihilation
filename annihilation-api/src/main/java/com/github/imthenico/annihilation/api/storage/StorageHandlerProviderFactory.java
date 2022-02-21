package com.github.imthenico.annihilation.api.storage;

import com.github.imthenico.annihilation.api.storage.impl.FileStorageHandlerProvider;
import com.github.imthenico.annihilation.api.storage.impl.SQLStorageHandlerProvider;

import java.util.Locale;

public interface StorageHandlerProviderFactory {

    static FileStorageHandlerProvider newFileProvider() {
        return new FileStorageHandlerProvider();
    }

    static SQLStorageHandlerProvider newSqlProvider() {
        return new SQLStorageHandlerProvider();
    }

    static StorageHandlerProvider of(String sourceDataType) {
        sourceDataType = sourceDataType.toLowerCase(Locale.ROOT);

        switch (sourceDataType) {
            case "file":
                return newFileProvider();
            case "sql":
                return newSqlProvider();
        }

        throw new IllegalArgumentException(String.format("invalid source type '%s'", sourceDataType));
    }
}
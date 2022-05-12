package com.github.imthenico.fastannihilation.storage;

import com.github.imthenico.fastannihilation.storage.impl.FileStorageHandlerProvider;
import com.google.gson.Gson;

import java.util.Locale;

public interface StorageHandlerProviderFactory {

    static FileStorageHandlerProvider newFileProvider(Gson gson) {
        return new FileStorageHandlerProvider(gson);
    }

    static StorageHandlerProvider newSqlProvider(Gson gson) {
        return null;
    }

    static StorageHandlerProvider of(Gson gson, String sourceDataType) {
        sourceDataType = sourceDataType.toLowerCase(Locale.ROOT);

        switch (sourceDataType) {
            case "file":
                return newFileProvider(gson);
            case "sql":
                return newSqlProvider(gson);
        }

        throw new IllegalArgumentException(String.format("invalid source type '%s'", sourceDataType));
    }
}
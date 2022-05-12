package com.github.imthenico.fastannihilation.repository.impl;

import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.repository.SerializedModel;
import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public class DirectoryRepository<T> extends AbstractRepository<T> {

    private final File folder;

    public DirectoryRepository(Executor executor, Gson gson, Class<T> modelClass, File folder) {
        super(executor, gson, modelClass);

        if (!folder.isDirectory() && !folder.mkdirs()) {
            throw new UnsupportedOperationException("Unable to create folder");
        }

        this.folder = folder;
    }

    @Override
    protected void saveData(String name, SerializedModel serializedModel) {
        try {
            File file = getFile(name, true);

            saveContent(file, serializedModel.getJsonInText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull JsonObject find(@NotNull String name) throws NoModelSuchException, InvalidContentException {
        try {
            File file = getFile(name, false);

            if (!file.exists())
                throw new NoModelSuchException(name);

            return fromText(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(@NotNull String name) {
        try {
            return getFile(name, false).delete();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public @NotNull Set<JsonObject> findAll() throws InvalidContentException {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null)
            return Collections.emptySet();

        Set<JsonObject> valuesFound = new HashSet<>();

        for (File file : files) {
            try {
                JsonObject jsonValue = fromText(file);
                valuesFound.add(jsonValue);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return valuesFound;
    }

    @Override
    public @NotNull Set<String> keys() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null)
            return Collections.emptySet();

        Set<String> keys = new HashSet<>();

        for (File file : files) {
            String name = file.getName();
            String normalizedName = name
                    .substring(0, name.length() - 5);

            keys.add(normalizedName);
        }

        return keys;
    }

    private File getFile(String key, boolean createIfAbsent) throws IOException {
        String name = key.endsWith(".json") ? key : key + ".json";

        File file = new File(folder, name);

        if (!file.exists() && createIfAbsent) {
            if (!file.createNewFile()) {
                throw new RuntimeException("Unable to create file '" + name + "'");
            }
        }

        return file;
    }

    private String getContent(File file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(file)
        );

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    private void saveContent(File file, String content) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(content);
        }
    }

    private JsonObject fromText(File file) throws IOException, InvalidContentException {
        String content = getContent(file);

        if (!(content.startsWith("{") || content.endsWith("}"))) {
            throw new InvalidContentException("Invalid json");
        }

        return JsonParser.parseString(content).getAsJsonObject();
    }
}
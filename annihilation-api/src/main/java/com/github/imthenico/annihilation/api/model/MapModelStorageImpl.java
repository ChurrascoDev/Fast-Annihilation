package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.exception.NoElementFoundException;
import com.github.imthenico.annihilation.api.util.CompletableFutures;
import com.github.imthenico.gmlib.GameMapHandler;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.ModelData;
import com.github.imthenico.gmlib.Serialized;
import com.github.imthenico.gmlib.exception.NoWorldFoundException;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.repository.exception.InvalidContentException;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MapModelStorageImpl implements MapModelStorage {

    private final ModelCache modelCache;
    private final AbstractRepository<JsonObject> modelDataRepository;
    private final GameMapHandler gameMapHandler;

    public MapModelStorageImpl(
            AbstractRepository<JsonObject> modelDataRepository,
            GameMapHandler gameMapHandler
    ) {
        this.modelCache = new SimpleModelCache();
        this.modelDataRepository = modelDataRepository;
        this.gameMapHandler = gameMapHandler;
    }

    @Override
    public MapModel<?> createModel(String name)
            throws NoModelSuchException, ClassNotFoundException, NoElementFoundException {
        try {
            JsonObject dataInJson = modelDataRepository.find(name);

            return fromJson(dataInJson);
        } catch (InvalidContentException | NoWorldFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MapModel<?>> getAllFromData()
            throws IllegalArgumentException, ClassNotFoundException, NoElementFoundException {
        try {
            List<MapModel<?>> models = new ArrayList<>();

            for (JsonObject jsonObject : modelDataRepository.findAll()) {
                models.add(fromJson(jsonObject));
            }

            return models;
        } catch (InvalidContentException | NoWorldFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<MapModel<?>> createModelAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return createModel(name);
            } catch (NoModelSuchException | NoElementFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<List<MapModel<?>>> getAllFromDataAsync() {
        return CompletableFutures.supplyAsync(() -> {
            try {
                return this.getAllFromData();
            } catch (ClassNotFoundException | NoElementFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Serialized save(MapModel<?> mapModel) {
        mapModel.getMainWorld().save();
        mapModel.getAdditionalWorlds().forEach(AWorld::save);

        Serialized serialized = gameMapHandler.serialize(mapModel);
        JsonObject jsonObject = serialized.getJsonObject();

        jsonObject.add("data-class-name", new JsonPrimitive(mapModel.getDataType().getName()));

        modelDataRepository.save(mapModel.getName(), serialized.getJsonObject());
        return serialized;
    }

    @Override
    public CompletableFuture<Serialized> asyncSave(MapModel<?> mapModel) {
        return CompletableFuture.supplyAsync(() -> save(mapModel));
    }

    @Override
    public ModelCache getCachedModels() {
        return modelCache;
    }

    @Override
    public AbstractRepository<JsonObject> getDataRepository() {
        return modelDataRepository;
    }

    @SuppressWarnings("unchecked")
    private MapModel<?> fromJson(JsonObject dataInJson) throws NoElementFoundException, NoWorldFoundException, ClassNotFoundException {
        JsonPrimitive modelDataClassName = dataInJson.getAsJsonPrimitive("data-class-name");

        if (modelDataClassName == null)
            throw new NoElementFoundException("data-class-name", "data-class-name");

        Class<ModelData> modelDataClass = (Class<ModelData>) Class.forName(modelDataClassName.getAsString());

        return gameMapHandler.fromJson(
                dataInJson,
                modelDataClass,
                worldRequest -> {}
        );
    }
}
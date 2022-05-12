package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.annihilation.api.exception.NoElementFoundException;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.Serialized;
import com.github.imthenico.repository.AbstractRepository;
import com.github.imthenico.repository.exception.NoModelSuchException;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MapModelStorage {

    /**
     * Finds a model data from storage and
     * creates a new model with the extracted data.
     *
     * @param name The model name
     * @return The deserialized model
     * @throws NoModelSuchException If no model data found
     * @throws NoElementFoundException If no class name found in data
     * @throws ClassNotFoundException If the class name in data is not a valid Class
     */
    MapModel<?> createModel(String name)
            throws NoModelSuchException, ClassNotFoundException, NoElementFoundException;

    /**
     * Finds all models in data storage and
     * creates a new model with the extracted data.
     *
     * @return The deserialized model
     * @throws NoElementFoundException If during a model creation no class name found in data
     * @throws ClassNotFoundException If during a model creation the class name in data is not a valid Class
     */
    List<MapModel<?>> getAllFromData()
            throws IllegalArgumentException, ClassNotFoundException, NoElementFoundException;

    /**
     * The {@link MapModelStorage#createModel(String)} but asynchronous.
     *
     * @param name The model name
     * @return A future that may be completed with the requested model
     */
    CompletableFuture<MapModel<?>> createModelAsync(String name);

    /**
     * The {@link MapModelStorage#getAllFromData()} but asynchronous.
     *
     * @return A future that may be completed with the all models in the data repository
     */
    CompletableFuture<List<MapModel<?>>> getAllFromDataAsync();

    Serialized save(MapModel<?> mapModel);

    CompletableFuture<Serialized> asyncSave(MapModel<?> mapModel);

    AbstractRepository<JsonObject> getDataRepository();

    ModelCache getCachedModels();

}
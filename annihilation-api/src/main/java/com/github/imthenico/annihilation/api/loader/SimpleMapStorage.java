package com.github.imthenico.annihilation.api.loader;

import com.github.imthenico.annihilation.api.exception.NoPropertiesFoundException;
import com.github.imthenico.annihilation.api.exception.UnknownWorldException;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.model.SimpleConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyMapping;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.provider.WorldTemplateLoader;
import com.github.imthenico.annihilation.api.concurrent.CompletableFutures;
import com.github.imthenico.annihilation.api.world.LoadedWorldTemplate;
import com.github.imthenico.simplecommons.data.key.SourceKey;
import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.NodeValueList;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.repository.AbstractRepository;
import com.github.imthenico.simplecommons.util.Pair;
import com.github.imthenico.simplecommons.value.AbstractValue;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SimpleMapStorage implements MapModelStorage {

    private final AbstractRepository<TreeNode> propertiesRepository;
    private final PropertyMapping propertyMapping;
    private final WorldTemplateLoader worldTemplateLoader;
    private final Scheduler scheduler;

    public SimpleMapStorage(
            AbstractRepository<TreeNode> propertiesRepository,
            PropertyMapping propertyMapping,
            WorldTemplateLoader worldTemplateLoader,
            Scheduler scheduler
    ) {
        this.propertiesRepository = propertiesRepository;
        this.propertyMapping = propertyMapping;
        this.worldTemplateLoader = worldTemplateLoader;
        this.scheduler = scheduler;
    }

    @Override
    public CompletableFuture<Integer> asyncDelete(SourceKey sourceKey) {
        return propertiesRepository.asyncDelete(sourceKey);
    }

    @Override
    public int delete(SourceKey sourceKey) {
        return propertiesRepository.delete(sourceKey);
    }

    @Override
    public CompletableFuture<ConfigurableModel> asyncFind(SourceKey sourceKey) {
        CompletableFuture<ConfigurableModel> futureModel = new CompletableFuture<>();

        CompletableFutures.runAsync(() -> getModel(sourceKey, true)
                .whenComplete((model, throwable) -> futureModel.complete(model)));

        return futureModel;
    }

    @Override
    public CompletableFuture<Set<ConfigurableModel>> asyncAllCollection() {
        return CompletableFutures.supplyAsync(() -> {
            Set<ConfigurableModel> models = new HashSet<>();

            for (SourceKey key : keys()) {
                CompletableFuture<ConfigurableModel> futureModel = getModel(key, true);

                try {
                    models.add(futureModel.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }

            return models;
        });
    }

    @Override
    public CompletableFuture<Set<SourceKey>> asyncKeyCollection() {
        return CompletableFutures.supplyAsync(this::keys);
    }

    @Override
    public ConfigurableModel usingId(SourceKey sourceKey)
            throws UnsupportedOperationException, NoPropertiesFoundException, UnknownWorldException {
        if (!Bukkit.isPrimaryThread())
            throw new UnsupportedOperationException("This method must be called in the main thread.");

        try {
            return getModel(sourceKey, false).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Set<ConfigurableModel> all() {
        Set<ConfigurableModel> models = new HashSet<>();

        for (SourceKey key : keys()) {
            models.add(usingId(key));
        }

        return models;
    }

    @Override
    public Set<SourceKey> keys() {
        return propertiesRepository.keys();
    }

    @Override
    public CompletableFuture<?> asyncSave(ConfigurableModel model, SourceKey sourceKey) {
        return CompletableFutures.runAsync(() -> save(model, sourceKey));
    }

    @Override
    public void save(ConfigurableModel model, SourceKey sourceKey) {
        model.getMainWorld().save();

        model.getWorlds().values().forEach(LoadedWorldTemplate::save);

        TreeNode serialized = propertyMapping.serializeProperties(model.getProperties());

        propertiesRepository.save(serialized, sourceKey);
    }

    private Pair<LoadedWorldTemplate, List<LoadedWorldTemplate>> getTemplates(String worldName, List<String> complementaryWorldNames) {
        LoadedWorldTemplate worldTemplate = getTemplate(worldName);
        List<LoadedWorldTemplate> complementaryWorlds = new ArrayList<>(complementaryWorldNames.size());

        for (String complementaryWorldName : complementaryWorldNames) {
            complementaryWorlds.add(getTemplate(complementaryWorldName));
        }

        return new Pair<>(worldTemplate, complementaryWorlds);
    }

    private CompletableFuture<ConfigurableModel> getModel(SourceKey sourceKey, boolean ensureSyncWorldLoading) {
        TreeNode mapData = getData(sourceKey);

        String worldName = mapData.get("world-name")
                .getAsSimpleValue().get()
                .getAsString().get();

        List<String> complementaryWorldNames = toList(String.class, mapData.get("complementary-worlds")
                .getAsArray().get());

        PropertiesContainer propertyHandler = propertyMapping.deserializeProperties(mapData);

        Supplier<ConfigurableModel> modelSupplier = () -> {
            Pair<LoadedWorldTemplate, List<LoadedWorldTemplate>> worldTemplates = getTemplates(
                    worldName,
                    complementaryWorldNames
            );

            ConfigurableModel model = new SimpleConfigurableModel(sourceKey.getKey(), worldTemplates.getLeft());

            worldTemplates.getRight().forEach(model::addWorld);

            model.getProperties().apply(propertyHandler);

            return model;
        };

        if (ensureSyncWorldLoading) {
            return scheduler.supply(modelSupplier, false);
        }

        return CompletableFuture.completedFuture(modelSupplier.get());
    }

    private TreeNode getData(SourceKey sourceKey) {
        TreeNode rawProperties = propertiesRepository.usingId(sourceKey);

        if (rawProperties == null)
            throw new NoPropertiesFoundException(sourceKey.getKey());

        ensureValueType("world-name", String.class, sourceKey::getKey, rawProperties);
        ensureArrayValueType("complementary-worlds", String.class, ArrayList::new, rawProperties);

        return rawProperties;
    }

    private <T> void ensureValueType(String key, Class<T> clazz, Supplier<T> def, TreeNode node) {
        NodeValue found = node.get(key);
        Object value = getFinalObject(found);

        if (!clazz.isInstance(value)) {
            node.set(key, def.get());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> toList(Class<T> clazz, NodeValueList nodeValues) {
        List<T> list = new ArrayList<>();

        for (NodeValue nodeValue : nodeValues) {
            Object value = getFinalObject(nodeValue);

            if (clazz.isInstance(value)) {
                list.add((T) value);
            }
        }

        return list;
    }

    private Object getFinalObject(NodeValue nodeValue) {
        Object value = nodeValue.getValue();

        if (value instanceof AbstractValue) {
            value = ((AbstractValue) value).getValue();
        }

        return value;
    }

    private <T> void ensureArrayValueType(String key, Class<T> clazz, Supplier<?> def, TreeNode node) {
        NodeValue found = node.get(key);
        Optional<NodeValueList> optionalNodeValues = found.getAsArray();

        if (!optionalNodeValues.isPresent()) {
            node.set(key, def.get());

            return;
        }

        NodeValueList nodeValues = optionalNodeValues.get();

        for (NodeValue nodeValue : nodeValues) {
            Object finalObject = getFinalObject(nodeValue);

            if (!clazz.isInstance(finalObject)) {
                node.set(key, def.get());
                return;
            }
        }
    }

    private LoadedWorldTemplate getTemplate(String name) {
        return worldTemplateLoader.newTemplate(name).orElseThrow(() -> new UnknownWorldException(name));
    }
}
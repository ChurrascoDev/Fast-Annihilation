package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.annihilation.api.editor.SetupContext;
import com.github.imthenico.annihilation.api.exception.TimeoutException;
import com.github.imthenico.annihilation.api.model.MapModelStorage;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.EditableMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.registry.ModelType;
import com.github.imthenico.annihilation.api.registry.ModelTypeRegistry;
import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.ModelData;
import com.github.imthenico.gmlib.exception.NoWorldFoundException;
import com.github.imthenico.gmlib.pool.WorldPool;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldPack;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Command(names = "model")
public class ConfigurableModelManagerCommand implements CommandClass {

    private final ModelTypeRegistry modelTypeRegistry;
    private final MapModelStorage mapModelStorage;
    private final ModelCache modelCache;
    private final WorldPool templatePool;
    private final ModelSetupManager modelSetupManager;

    public ConfigurableModelManagerCommand(
            ModelTypeRegistry modelTypeRegistry,
            MapModelStorage mapModelStorage,
            WorldPool templatePool,
            ModelSetupManager modelSetupManager
    ) {
        this.modelTypeRegistry = modelTypeRegistry;
        this.mapModelStorage = mapModelStorage;
        this.modelCache = mapModelStorage.getCachedModels();
        this.templatePool = templatePool;
        this.modelSetupManager = modelSetupManager;
    }

    @Command(names = "create")
    public boolean createModel(
            @Sender Player player,
            String name,
            String typeName,
            String templateName
    ) throws NoWorldFoundException {
        ModelType<?> modelType = modelTypeRegistry.get(typeName);

        if (modelType == null) {
            player.sendMessage(typeName + " is not registered.");
            return true;
        }

        if (modelCache.has(name)) {
            player.sendMessage("There's already a model registered with this name.");
            return true;
        }

        try {
            WorldPack worldPack = WorldPack.fromPool(
                    templatePool,
                    templateName,
                    Collections.emptyList(),
                    (map) -> {}
            ).get(10, TimeUnit.SECONDS);

            ModelData mapData = modelType.createNewData();

            MapModel<?> model = MapModel.of(
                    name,
                    mapData,
                    worldPack
            );

            modelCache.addModel(model);

            player.sendMessage("Model created successfully");
            player.sendMessage("Model info:");
            player.sendMessage(String.format("Name: %s, Type: %s, World: %s", name, typeName, templateName));
        } catch (InterruptedException | java.util.concurrent.TimeoutException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Command(names = "edit")
    public boolean editModel(
            @Sender AnniPlayer anniPlayer,
            String modelName
    ) {
        MapModel<? extends EditableMapData> mapModel = modelCache.getModel(modelName);
        Player player = anniPlayer.getPlayer();

        if (mapModel == null) {
            player.sendMessage("Invalid model name.");
            return true;
        }

        try {
            modelSetupManager.setupModel(anniPlayer, mapModel);
            World world = mapModel.getMainWorld().handle();

            player.teleport(world.getSpawnLocation());

            player.sendMessage("You're now editing " + mapModel.getName());
        } catch (UnsupportedOperationException e) {
            player.sendMessage("You're already editing a map");
        }

        return true;
    }

    @Command(names = "leave")
    public boolean saveChanges(
            @Sender AnniPlayer player, boolean saveChanges
    ) {
        SetupContext<?> context = modelSetupManager.removePlayerFromSession(player);

        if (context == null)
            return true;

        MapModel<? extends EditableMapData> mapModel = context.getEditingTarget();

        mapModel.allWorlds().forEach(AWorld::save);
        mapModel.getData().accept(context.getChangesProduced());

        if (saveChanges) {
            mapModelStorage.asyncSave(context.getEditingTarget())
                    .exceptionally((exc) -> {
                        if (exc instanceof TimeoutException) {
                            TimeoutException exception = (TimeoutException) exc;

                            player.getPlayer().sendMessage(String.format("Unable to save model after %s seconds", exception.getTime().getSeconds()));
                        }
                        return null;
                    })
                    .whenComplete((obj, exc) -> player.getPlayer().sendMessage("Model saved correctly"));
        }

        return true;
    }

    @Command(names = "list")
    public boolean listModels(@Sender Player player) {
        if (modelCache.count() == 0) {
            player.sendMessage("No models to display :(");
        } else {
            for (MapModel<?> value : modelCache) {
                player.sendMessage(formatModel(value));
            }
        }

        return true;
    }

    @Command(names = "tlist")
    public boolean listModelDataTypes(@Sender Player player) {
        modelTypeRegistry.getAll().forEach((k, v) -> {
            player.sendMessage("Model Type Name: " + k);
            player.sendMessage("Description: " + v.getDescription());
        });

        return true;
    }

    private SetupContext<?> getSetupContext(Player player) {
        SetupContext<?> setupContext = modelSetupManager.getSession(player);

        if (setupContext == null) {
            player.sendMessage("You're not editing any map.");
        }

        return setupContext;
    }

    private String formatModel(MapModel<?> model) {
        String format = "%s - %s - %s\n  Additional worlds: %s";

        String modelTypeName = modelTypeRegistry.getName(model.getDataType());

        List<String> additionalWorldNames = new ArrayList<>();

        model.getAdditionalWorlds().forEach(aWorld -> additionalWorldNames.add(aWorld.getName()));
        return String.format(
                format,
                model.getName(),
                modelTypeName,
                model.getMainWorld().getName(),
                additionalWorldNames
        );
    }
}
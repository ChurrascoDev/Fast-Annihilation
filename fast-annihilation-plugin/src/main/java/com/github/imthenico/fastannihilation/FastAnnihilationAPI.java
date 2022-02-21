package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.property.PropertyMapping;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.service.ConfigurableModelService;
import com.github.imthenico.annihilation.api.service.GameService;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.simplecommons.bukkit.service.PluginServiceRegistry;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;

public class FastAnnihilationAPI implements AnnihilationAPI {

    private final UtilityPack utilityPack;
    private final PlayerRegistry playerRegistry;
    private final PluginServiceRegistry pluginServiceRegistry;
    private final Scheduler scheduler;
    private final PropertyMapping propertyMapping;
    private LocationReference lobbySpawn;

    public FastAnnihilationAPI(
            UtilityPack utilityPack,
            PlayerRegistry playerRegistry,
            PluginServiceRegistry pluginServiceRegistry,
            PropertyMapping propertyMapping,
            Scheduler scheduler
    ) {
        this.utilityPack = utilityPack;
        this.playerRegistry = playerRegistry;
        this.pluginServiceRegistry = pluginServiceRegistry;
        this.propertyMapping = propertyMapping;
        this.scheduler = scheduler;
    }

    @Override
    public UtilityPack utilities() {
        return utilityPack;
    }

    @Override
    public PlayerRegistry playerCache() {
        return playerRegistry;
    }

    @Override
    public GameService gameService() {
        return pluginServiceRegistry.getService(GameService.class);
    }

    @Override
    public ConfigurableModelService modelService() {
        return pluginServiceRegistry.getService(ConfigurableModelService.class);
    }

    @Override
    public PropertyMapping getPropertyMapping() {
        return propertyMapping;
    }

    @Override
    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public LocationReference getLobbySpawn() {
        return Validate.defIfNull(lobbySpawn, () -> Bukkit.getWorlds().get(0).getSpawnLocation());
    }

    @Override
    public void setLobbySpawn(LocationReference lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }
}
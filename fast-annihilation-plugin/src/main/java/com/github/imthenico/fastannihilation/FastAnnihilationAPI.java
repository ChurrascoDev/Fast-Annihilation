package com.github.imthenico.fastannihilation;

import com.github.imthenico.annihilation.api.AnnihilationAPI;
import com.github.imthenico.annihilation.api.game.AnnihilationGameFactory;
import com.github.imthenico.annihilation.api.game.SimpleGameInstanceManager;
import com.github.imthenico.annihilation.api.game.SimpleGameRoomFactory;
import com.github.imthenico.annihilation.api.map.ConfigurableModelManager;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.game.GameInstanceManager;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.UtilityPack;
import com.github.imthenico.annihilation.api.world.LocationReference;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;

public class FastAnnihilationAPI implements AnnihilationAPI {

    private final UtilityPack utilityPack;
    private final PlayerRegistry playerRegistry;
    private final GameInstanceManager gameRegistry;
    private final AnnihilationGameFactory annihilationGameFactory;
    private final ConfigurableModelManager configurableModelManager;
    private final Scheduler scheduler;
    private LocationReference lobbySpawn;

    public FastAnnihilationAPI(
            UtilityPack utilityPack,
            PlayerRegistry playerRegistry,
            ConfigurableModelManager configurableModelManager,
            Scheduler scheduler
    ) {
        this.utilityPack = utilityPack;
        this.playerRegistry = playerRegistry;
        this.configurableModelManager = configurableModelManager;
        this.scheduler = scheduler;

        this.gameRegistry = new SimpleGameInstanceManager();
        this.annihilationGameFactory = new SimpleGameRoomFactory(this, gameRegistry);
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
    public GameInstanceManager gameRegistry() {
        return gameRegistry;
    }

    @Override
    public AnnihilationGameFactory gameFactory() {
        return annihilationGameFactory;
    }

    @Override
    public ConfigurableModelManager getMapManager() {
        return configurableModelManager;
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
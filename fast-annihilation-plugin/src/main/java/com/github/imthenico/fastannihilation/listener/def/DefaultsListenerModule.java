package com.github.imthenico.fastannihilation.listener.def;

import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.fastannihilation.listener.ListenerModule;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import com.github.imthenico.annihilation.api.world.LocationReference;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class DefaultsListenerModule implements ListenerModule {

    private final ModelSetupManager modelSetupManager;
    private final PlayerRegistry playerRegistry;
    private final LocationReference hubSpawnReference;
    private final ScoreboardService scoreboardService;

    public DefaultsListenerModule(
            ModelSetupManager modelSetupManager,
            PlayerRegistry playerRegistry,
            LocationReference hubSpawnReference,
            ScoreboardService scoreboardService
    ) {
        this.modelSetupManager = modelSetupManager;
        this.playerRegistry = playerRegistry;
        this.hubSpawnReference = hubSpawnReference;
        this.scoreboardService = scoreboardService;
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new PlayerJoinListener(playerRegistry, scoreboardService),
                new PlayerTeleportListener(modelSetupManager),
                new PlayerLeaveSetupListener(hubSpawnReference),
                new PlayerQuitListener(modelSetupManager, playerRegistry)
        );
    }
}
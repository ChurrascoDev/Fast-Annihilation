package com.github.imthenico.fastannihilation.listener.def.game;

import com.github.imthenico.fastannihilation.listener.ListenerModule;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class GameListenerModule implements ListenerModule {
    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new PlayerJoinGameListener(),
                new PlayerLeaveGameListener()
        );
    }
}
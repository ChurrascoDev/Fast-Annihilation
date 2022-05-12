package com.github.imthenico.fastannihilation.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.List;

public interface ListenerModule {

    List<Listener> getListeners();

    default void register(Plugin plugin) {
        for (Listener listener : getListeners()) {
            boolean registered = false;
            for (RegisteredListener registeredListener : HandlerList.getRegisteredListeners(plugin)) {
                if (listener.equals(registeredListener.getListener())) {
                    registered = true;
                    break;
                }
            }

            if (!registered) {
                Bukkit.getPluginManager().registerEvents(listener, plugin);
            }
        }
    }
}
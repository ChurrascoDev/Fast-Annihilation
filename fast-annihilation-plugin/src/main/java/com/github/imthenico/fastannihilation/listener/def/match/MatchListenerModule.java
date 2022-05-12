package com.github.imthenico.fastannihilation.listener.def.match;

import com.github.imthenico.fastannihilation.listener.ListenerModule;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import me.yushust.message.MessageHandler;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.List;

public class MatchListenerModule implements ListenerModule {

    private final String expectedMatchTypeName;
    private final MessageHandler messageHandler;
    private final Scheduler scheduler;
    private final PlayerRegistry playerRegistry;

    public MatchListenerModule(String expectedMatchTypeName, MessageHandler messageHandler, Scheduler scheduler, PlayerRegistry playerRegistry) {
        this.expectedMatchTypeName = expectedMatchTypeName;
        this.messageHandler = messageHandler;
        this.scheduler = scheduler;
        this.playerRegistry = playerRegistry;
    }

    @Override
    public List<Listener> getListeners() {
        return Arrays.asList(
                new MatchEventsListener(expectedMatchTypeName, messageHandler),
                new BlockBreakListener(playerRegistry),
                new PlayerDeathListener(expectedMatchTypeName, playerRegistry, messageHandler, scheduler),
                new PlayerJoinMatchListener(expectedMatchTypeName),
                new PlayerLeaveMatchListener(expectedMatchTypeName),
                new PlayerSelectTeamListener(messageHandler),
                new PlayerJoinTeamListener(),
                new PlayerVoteListener(messageHandler)
        );
    }
}
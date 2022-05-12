package com.github.imthenico.annihilation.api.event.game;

import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerLeaveRoomEvent extends Event {

    private final static HandlerList HANDLER_LIST = new HandlerList();

    private final AnniPlayer player;
    private final GameRoom room;

    public PlayerLeaveRoomEvent(GameRoom room, AnniPlayer player) {
        this.player = player;
        this.room = room;
    }

    public AnniPlayer getPlayer() {
        return player;
    }

    public GameRoom getRoom() {
        return room;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
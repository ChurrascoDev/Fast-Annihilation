package com.github.imthenico.fastannihilation.listener.def.game;

import com.github.imthenico.annihilation.api.event.game.PlayerJoinRoomEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.game.GameState;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import net.hexaway.board.abstraction.model.ScoreboardPack;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.github.imthenico.fastannihilation.service.ScoreboardServiceImpl.*;

public class PlayerJoinGameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinRoomEvent event) {
        AnniPlayer anniPlayer = event.getPlayer();

        GameRoom gameRoom = event.getRoom();
        Game game = gameRoom.game();

        getByState(game.calculateState()).install(anniPlayer.getComplexBoard());
    }

    private static ScoreboardPack getByState(GameState state) {
        switch (state) {
            case WAITING:
                return WAITING_GAME_BOARD_MODEL;
            case STARTING:
                return STARTING_GAME_BOARD_MODEL;
            case IN_GAME:
            case ENDING:
                return GAME_BOARD_MODEL;
        }

        throw new RuntimeException();
    }
}
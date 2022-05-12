package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.game.GameFactory;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.game.GameManager;
import com.github.imthenico.annihilation.api.model.ModelCache;
import com.github.imthenico.annihilation.api.model.lobby.GameLobbyData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(names = {"game", "gamemanager"})
public class GameInstanceManagerCommand implements CommandClass {

    private final GameManager gameManager;
    private final GameFactory gameFactory;
    private final ModelCache modelCache;

    public GameInstanceManagerCommand(
            GameManager gameManager,
            GameFactory gameFactory,
            ModelCache modelCache
    ) {
        this.gameManager = gameManager;
        this.gameFactory = gameFactory;
        this.modelCache = modelCache;
    }

    @Command(names = "create")
    public boolean createGame(
            @Sender Player player,
            String gameId,
            String lobbyModelName,
            String gameTypeName
    ) {
        if (gameManager.isRegistered(gameId)) {
            player.sendMessage(ChatColor.RED + "There's already an instance registered with that name");
            return true;
        }

        MapModel<? extends GameLobbyData> model = getModel(player, lobbyModelName);

        if (model == null)
            return true;

        try {
            GameRoom gameRoom = gameFactory.newGame(gameId, gameTypeName, model);
            gameRoom.setEnabled(true);
            gameManager.registerGame(gameRoom);

            player.sendMessage(ChatColor.GREEN + "Game registered successfully");
            player.sendMessage(String.format(ChatColor.AQUA + "Name: %s, Lobby Model: %s, Game Type: %s", gameId, lobbyModelName, gameTypeName));
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid match type name");
        }

        return true;
    }

    @Command(names = "join")
    public boolean joinGame(
            @Sender AnniPlayer player,
            String gameId
    ) {
        GameRoom gameRoom = gameManager.getGame(gameId);

        if (gameRoom == null) {
            player.sendMessage("&cInvalid game id", true);
            return true;
        }

        if (!gameRoom.isEnabled()) {
            player.sendMessage("&cThis room is disabled", true);
            return true;
        }

        player.sendMessage("&aJoining to " + gameId, true);

        if (gameRoom.playerCount() >= gameRoom.getRules().getMaxPlayers()) {
            if (!player.getPlayer().hasPermission("anni.bypass.game-limit")) {
                player.sendMessage("&cThe room is full", true);
                return false;
            }
        }

        gameRoom.join(player);
        return true;
    }

    @Command(names = "leave")
    public boolean leaveGame(
            @Sender AnniPlayer player,
            String gameId
    ) {
        GameRoom gameRoom = player.getPlayingRoom();

        if (gameRoom == null) {
            player.sendMessage("&cYou are not in any room", true);
            return true;
        }

        player.sendMessage("&cLeaving from " + gameId, true);
        gameRoom.leave(player);
        return true;
    }

    @SuppressWarnings("rawtypes, unchecked")
    private MapModel<? extends GameLobbyData> getModel(Player player, String name) {
        MapModel mapModel = modelCache.getModel(name);

        if (mapModel == null) {
            player.sendMessage(ChatColor.RED + "Invalid model name");
        } else if (mapModel.getData() instanceof GameLobbyData) {
            return (MapModel<? extends GameLobbyData>) mapModel;
        }

        player.sendMessage(ChatColor.RED + "Model data is not apt for lobbies");
        return null;
    }
}
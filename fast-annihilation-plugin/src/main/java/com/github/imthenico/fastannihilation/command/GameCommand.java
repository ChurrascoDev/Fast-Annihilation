package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.match.authorization.AuthorizationResult;
import com.github.imthenico.annihilation.api.message.MessagePath;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.lang.CustomMessageHandler;
import com.github.imthenico.gmlib.MapModel;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.yushust.message.util.ReplacePack;

@Command(names = "game")
public class GameCommand implements CommandClass {

    private final CustomMessageHandler messageHandler;

    public GameCommand(CustomMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Command(names = "start")
    public boolean startGame(@Sender AnniPlayer anniPlayer, String modelName) {
        Game game = anniPlayer.getPlayingGame();

        if (game == null) {
            anniPlayer.sendMessage("&cYou're not in any game", true);
            return true;
        }

        if (game.runningMatch() != null) {
            anniPlayer.sendMessage("&cThere's already a match running in this game", true);
            return true;
        }

        MapModel<? extends MatchMapData> suggestedModel = game
                .getMatchMapModelProvider()
                .getModel(modelName);

        if (suggestedModel == null) {
            anniPlayer.sendMessage("'" + modelName + "' doesn't exists", true);
            return true;
        }

        AuthorizationResult result = game.startMatch(suggestedModel);

        if (!result.isAuthorized()) {
            MessagePath messagePath = result.getReasonMessage();

            messageHandler.dispatch(anniPlayer, "default", messagePath, ReplacePack.EMPTY);
        } else {
            messageHandler.send(game.room().getPlayers((p) -> true), "on-match-start");
        }

        return true;
    }
}
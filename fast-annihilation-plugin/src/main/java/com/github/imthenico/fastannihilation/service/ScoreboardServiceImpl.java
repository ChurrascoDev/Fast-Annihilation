package com.github.imthenico.fastannihilation.service;

import com.github.imthenico.annihilation.api.board.CompositeFrameInterceptor;
import com.github.imthenico.annihilation.api.scoreboard.UnDeletableComplexBoard;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import com.github.imthenico.fastannihilation.scoreboard.PAPIInterceptor;
import net.hexaway.board.SimpleComplexBoardManager;
import net.hexaway.board.abstraction.ComplexBoard;
import net.hexaway.board.abstraction.ComplexBoardManager;
import net.hexaway.board.abstraction.LineAlgorithm;
import net.hexaway.board.abstraction.ScoreboardElement;
import net.hexaway.board.abstraction.model.ScoreboardModelBuilder;
import net.hexaway.board.abstraction.model.ScoreboardPack;
import org.bukkit.entity.Player;
import com.github.imthenico.fastannihilation.scoreboard.ColorInterceptor;

public class ScoreboardServiceImpl implements ScoreboardService {

    public static final ScoreboardPack LOBBY_BOARD_MODEL;
    public static final ScoreboardPack WAITING_GAME_BOARD_MODEL;
    public static final ScoreboardPack STARTING_GAME_BOARD_MODEL;
    public static final ScoreboardPack GAME_BOARD_MODEL;

    static {
        LOBBY_BOARD_MODEL = new ScoreboardModelBuilder()
                .title(ScoreboardElement.simple("&6Annihilation"))
                .line(14, "Hi &a%player_name%&r!")
                .line(13, "&e")
                .line(12, "&bLobby: %anniplayer_lobby%")
                .line(11, "&b")
                .line(10, "&b%server_ip%")
                .build();

        WAITING_GAME_BOARD_MODEL = new ScoreboardModelBuilder()
                .title(ScoreboardElement.simple("&6Annihilation"))
                .line(14, "Player: &b%player_name%")
                .line(13, "Room: %game_id%")
                .line(12, "&e")
                .line(11, "Waiting for players...")
                .line(10, "Players: &b%game_players%&r/&a%game_min_players%")
                .line(9, "&cRed: %game_team_red_count%")
                .line(8, "&eYellow: %game_team_yellow_count%")
                .line(7, "&aGreen: %game_team_green_count%")
                .line(6, "&9Blue: %game_team_blue_count%")
                .line(5, "&b")
                .line(4, "&b%server_ip%")
                .build();

        STARTING_GAME_BOARD_MODEL = new ScoreboardModelBuilder()
                .title(ScoreboardElement.simple("&6Annihilation"))
                .line(14, "Player: &b%player_name%")
                .line(13, "Room: %game_id%")
                .line(12, "&e")
                .line(11, "Time to start: &a%game_time%")
                .line(10, "&b")
                .line(9, "&cRed: %game_team_red_count%")
                .line(8, "&eYellow: %game_team_yellow_count%")
                .line(7, "&aGreen: %game_team_green_count%")
                .line(6, "&9Blue: %game_team_blue_count%")
                .line(5, "&a")
                .line(4, "&b%server_ip%")
                .build();

        GAME_BOARD_MODEL = new ScoreboardModelBuilder()
                .title(ScoreboardElement.simple("&6Annihilation"))
                .line(14, "Player: &b%player_name%")
                .line(13, "&a")
                .line(12, "Room: %game_id%")
                .line(11, "&cRed &anexus: %nexus_red_elegant_health%")
                .line(10, "&eYellow &anexus: %nexus_yellow_elegant_health%")
                .line(9, "&aGreen &anexus: %nexus_green_elegant_health%")
                .line(8, "&bBlue &anexus: %nexus_blue_elegant_health%")
                .line(7, "&b")
                .line(5, "Current Phase: %match_current_phase%")
                .line(4, "Next Phase: %match_next_phase_time%")
                .line(3, "&f")
                .line(2, "&b%server_ip%")
                .build();
    }

    private final CompositeFrameInterceptor frameInterceptor = new CompositeFrameInterceptor();
    private final ComplexBoardManager boardManager;

    public ScoreboardServiceImpl() {
        this.boardManager = new SimpleComplexBoardManager();
        boardManager.stop();
        frameInterceptor.addInterceptor(new ColorInterceptor());
        frameInterceptor.addInterceptor(new PAPIInterceptor());
    }

    @Override
    public CompositeFrameInterceptor getFrameInterceptor() {
        return frameInterceptor;
    }

    @Override
    public ComplexBoard displayBoard(Player player) {
        ComplexBoard board = getBoard(player);

        if (board != null) {
            board.reShow();
        } else {
            board = boardManager.createBoard(player, LineAlgorithm.ANY, frameInterceptor);
        }

        return board;
    }

    @Override
    public ComplexBoard getBoard(Player player) {
        return boardManager.getBoard(player)
                .map(UnDeletableComplexBoard::new)
                .orElse(null);
    }

    @Override
    public void start() {
        boardManager.resume();
    }

    @Override
    public void end() {
        boardManager.stop();
    }

}
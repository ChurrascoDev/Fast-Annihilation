package com.github.imthenico.fastannihilation.service;

import com.github.imthenico.annihilation.api.board.CompositeFrameInterceptor;
import com.github.imthenico.annihilation.api.service.ScoreboardService;
import net.hexaway.board.SimpleComplexBoardManager;
import net.hexaway.board.abstraction.ComplexBoard;
import net.hexaway.board.abstraction.ComplexBoardManager;
import net.hexaway.board.abstraction.LineAlgorithm;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ScoreboardServiceImpl implements ScoreboardService {

    private final CompositeFrameInterceptor frameInterceptor = new CompositeFrameInterceptor();
    private final ComplexBoardManager boardManager;

    public ScoreboardServiceImpl() {
        this.boardManager = new SimpleComplexBoardManager();
        boardManager.stop();
    }

    @Override
    public CompositeFrameInterceptor getFrameInterceptor() {
        return frameInterceptor;
    }

    @Override
    public void displayBoard(Player player) {
        Optional<ComplexBoard> boardOptional = boardManager.getBoard(player);

        if (boardOptional.isPresent()) {
            boardOptional.get().reShow();
        } else {
            boardManager.createBoard(player, LineAlgorithm.ANY, frameInterceptor);
        }
    }

    @Override
    public ComplexBoard getBoard(Player player) {
        return boardManager.getBoard(player).orElse(null);
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
package com.github.imthenico.annihilation.api.service;

import com.github.imthenico.annihilation.api.board.CompositeFrameInterceptor;
import net.hexaway.board.abstraction.ComplexBoard;
import org.bukkit.entity.Player;

public interface ScoreboardService extends Service {

    CompositeFrameInterceptor getFrameInterceptor();

    ComplexBoard displayBoard(Player player);

    ComplexBoard getBoard(Player player);

}
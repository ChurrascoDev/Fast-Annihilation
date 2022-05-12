package com.github.imthenico.fastannihilation.scoreboard;

import com.github.imthenico.annihilation.api.util.Formatting;
import net.hexaway.board.abstraction.FrameInterceptor;
import org.bukkit.entity.Player;

public class ColorInterceptor implements FrameInterceptor {
    @Override
    public String interceptLineFrame(String s, Player player) {
        return Formatting.colorize(s);
    }

    @Override
    public String interceptTitleFrame(String s, Player player) {
        return Formatting.colorize(s);
    }
}
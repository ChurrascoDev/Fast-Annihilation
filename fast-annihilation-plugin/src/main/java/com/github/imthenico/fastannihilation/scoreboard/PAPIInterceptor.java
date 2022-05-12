package com.github.imthenico.fastannihilation.scoreboard;

import me.clip.placeholderapi.PlaceholderAPI;
import net.hexaway.board.abstraction.FrameInterceptor;
import org.bukkit.entity.Player;

public class PAPIInterceptor implements FrameInterceptor {

    @Override
    public String interceptLineFrame(String s, Player player) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }

    @Override
    public String interceptTitleFrame(String s, Player player) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }
}
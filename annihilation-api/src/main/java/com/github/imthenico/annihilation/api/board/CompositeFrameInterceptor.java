package com.github.imthenico.annihilation.api.board;

import net.hexaway.board.abstraction.FrameInterceptor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CompositeFrameInterceptor implements FrameInterceptor {

    private final List<FrameInterceptor> frameInterceptors = new ArrayList<>();

    @Override
    public String interceptLineFrame(String s, Player player) {
        Iterator<FrameInterceptor> iterator = frameInterceptors.iterator();
        while (iterator.hasNext()) {
            FrameInterceptor frameInterceptor = iterator.next();
            s = frameInterceptor.interceptLineFrame(s, player);

            iterator.remove();
            Objects.requireNonNull(s, "Interceptor returns null");
        }

        return s;
    }

    @Override
    public String interceptTitleFrame(String s, Player player) {
        Iterator<FrameInterceptor> iterator = frameInterceptors.iterator();
        while (iterator.hasNext()) {
            FrameInterceptor frameInterceptor = iterator.next();
            s = frameInterceptor.interceptTitleFrame(s, player);

            iterator.remove();
            Objects.requireNonNull(s, "Interceptor returns null");
        }

        return s;
    }

    public void addInterceptor(FrameInterceptor frameInterceptor) {
        frameInterceptors.add(Objects.requireNonNull(frameInterceptor));
    }
}
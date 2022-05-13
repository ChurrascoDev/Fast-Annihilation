package com.github.imthenico.fastannihilation.tests;

import net.hexaway.board.abstraction.ComplexBoard;
import net.hexaway.board.abstraction.FrameInterceptor;
import net.hexaway.board.abstraction.LineHandler;
import net.hexaway.board.abstraction.ScoreboardElement;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class FakeComplexBoard implements ComplexBoard {
    @Override
    public ScoreboardElement setComplexTitle(ScoreboardElement scoreboardElement) {
        return null;
    }

    @Override
    public ScoreboardElement getComplexTitle() {
        return null;
    }

    @Override
    public ScoreboardElement setComplexLine(int i, ScoreboardElement scoreboardElement) {
        return null;
    }

    @Override
    public ScoreboardElement getComplexLine(int i) {
        return null;
    }

    @Override
    public ScoreboardElement removeComplexLine(int i) {
        return null;
    }

    @Override
    public Map<Integer, ScoreboardElement> getComplexLines() {
        return null;
    }

    @Override
    public void clearComplexLines() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setTitle(String s) {

    }

    @Override
    public String getShowingTitle() {
        return null;
    }

    @Override
    public void setLines(List<String> list) {

    }

    @Override
    public void setLine(int i, String s) {

    }

    @Override
    public LineHandler.Context getLine(int i) {
        return null;
    }

    @Override
    public void removeLine(int i) {

    }

    @Override
    public List<LineHandler.Context> getShowingLines() {
        return null;
    }

    @Override
    public FrameInterceptor frameInterceptor() {
        return null;
    }

    @Override
    public void clearShowingLines() {

    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public void delete() {

    }

    @Override
    public boolean isDeleted() {
        return false;
    }

    @Override
    public void reShow() {

    }

    @Override
    public void tick() {

    }
}
package com.github.imthenico.annihilation.api.scoreboard;

import net.hexaway.board.abstraction.ComplexBoard;
import net.hexaway.board.abstraction.FrameInterceptor;
import net.hexaway.board.abstraction.LineHandler;
import net.hexaway.board.abstraction.ScoreboardElement;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class UnDeletableComplexBoard implements ComplexBoard {

    private final ComplexBoard delegate;

    public UnDeletableComplexBoard(ComplexBoard delegate) {
        this.delegate = delegate;
    }

    @Override
    public ScoreboardElement setComplexTitle(ScoreboardElement scoreboardElement) {
        return delegate.setComplexTitle(scoreboardElement);
    }

    @Override
    public ScoreboardElement getComplexTitle() {
        return delegate.getComplexTitle();
    }

    @Override
    public ScoreboardElement setComplexLine(int i, ScoreboardElement scoreboardElement) {
        return delegate.setComplexLine(i, scoreboardElement);
    }

    @Override
    public ScoreboardElement getComplexLine(int i) {
        return delegate.getComplexLine(i);
    }

    @Override
    public ScoreboardElement removeComplexLine(int i) {
        return delegate.removeComplexLine(i);
    }

    @Override
    public Map<Integer, ScoreboardElement> getComplexLines() {
        return delegate.getComplexLines();
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void setTitle(String s) {
        delegate.setTitle(s);
    }

    @Override
    public String getShowingTitle() {
        return delegate.getShowingTitle();
    }

    @Override
    public void setLines(List<String> list) {
        delegate.setLines(list);
    }

    @Override
    public void setLine(int i, String s) {
        delegate.setLine(i, s);
    }

    @Override
    public LineHandler.Context getLine(int i) {
        return delegate.getLine(i);
    }

    @Override
    public void removeLine(int i) {
        delegate.removeLine(i);
    }

    @Override
    public List<LineHandler.Context> getShowingLines() {
        return delegate.getShowingLines();
    }

    @Override
    public FrameInterceptor frameInterceptor() {
        return delegate.frameInterceptor();
    }

    @Override
    public void clearShowingLines() {
        delegate.clearShowingLines();
    }

    @Override
    public Player getPlayer() {
        return delegate.getPlayer();
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("cannot delete this board");
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
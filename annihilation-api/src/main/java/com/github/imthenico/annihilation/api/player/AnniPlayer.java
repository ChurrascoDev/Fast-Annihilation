package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.lang.LangHolder;
import com.github.imthenico.annihilation.api.scoreboard.UnDeletableComplexBoard;
import com.github.imthenico.annihilation.api.util.Formatting;
import net.hexaway.board.abstraction.ComplexBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public final class AnniPlayer implements LangHolder {

    private final UUID uuid;
    private final ComplexBoard complexBoard;
    private final Supplier<Player> playerSupplier;
    private GameRoom playingRoom;
    private String lang;

    public AnniPlayer(Player player, ComplexBoard complexBoard) {
        this(() -> {
            UUID uuid = player.getUniqueId();

            return Bukkit.getPlayer(uuid);
        }, complexBoard);
    }

    public AnniPlayer(Supplier<Player> playerSupplier, ComplexBoard complexBoard) {
        this.playerSupplier = Objects.requireNonNull(playerSupplier);
        this.uuid = playerSupplier.get().getUniqueId();
        this.complexBoard = new UnDeletableComplexBoard(Objects.requireNonNull(complexBoard));
    }

    @Override
    public @Nullable String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public UUID getId() {
        return uuid;
    }

    public String getName() {
        return getPlayer().getName();
    }

    public Player getPlayer() {
        return playerSupplier.get();
    }

    public GameRoom getPlayingRoom() {
        return playingRoom;
    }

    public ComplexBoard getComplexBoard() {
        return complexBoard;
    }

    public Game getPlayingGame() {
        if (playingRoom == null)
            return null;

        return playingRoom.game();
    }

    public void handleInternalGameJoin(GameRoom game) {
        if (!game.isWithin(this)) {
            throw new IllegalStateException("Register player after handle game join.");
        }

        this.playingRoom = Objects.requireNonNull(game);
    }

    public void handleInternalGameLeave(GameRoom game) {
        if (playingRoom != game) {
            throw new IllegalArgumentException("game != playingGame");
        }

        if (game.isWithin(this)) {
            throw new IllegalStateException("Remove player after handle game leave.");
        }

        this.playingRoom = null;
    }

    public void sendMessage(String message, boolean colorize) {
        if (colorize)
            message = Formatting.colorize(message);

        getPlayer().sendMessage(message);
    }
}
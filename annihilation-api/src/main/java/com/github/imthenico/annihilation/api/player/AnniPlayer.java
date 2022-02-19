package com.github.imthenico.annihilation.api.player;

import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.lang.LangHolder;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public final class AnniPlayer implements LangHolder {

    private final UUID uuid;
    private GameInstance playingGame;
    private String lang;

    public AnniPlayer(Player player) {
        this.uuid = Validate.notNull(player.getUniqueId());
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
        return Bukkit.getPlayer(uuid);
    }

    public GameInstance getPlayingGame() {
        return playingGame;
    }

    public void handleInternalGameJoin(GameInstance game) {
        this.playingGame = Validate.notNull(game);
        Validate.isTrue(game.isInGame(this), "Register player after handle game join.");
    }

    public void handleInternalGameLeave(GameInstance game) {
        Validate.isTrue(this.playingGame == game, "game != playingGame");
        Validate.isTrue(!game.isInGame(this), "Remove player after handle game leave.");

        this.playingGame = null;
    }
}
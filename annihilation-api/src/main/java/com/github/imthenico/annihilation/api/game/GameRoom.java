package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.model.lobby.GameLobby;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.player.AnniPlayer;

import java.util.List;
import java.util.function.Predicate;

public interface GameRoom {

    GameLobby getLobby();

    String id();

    MatchAuthorizer getMatchAuthorizer();

    Game game();

    void join(AnniPlayer player);

    void leave(AnniPlayer player);

    boolean isWithin(AnniPlayer player);

    List<AnniPlayer> getPlayers(Predicate<AnniPlayer> filter);

    int playerCount();

    void restoreLogic();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    Rules getRules();

    Options getOptions();

    /**
     * This method is used to identify different types of
     * games with different game functions, characteristics, etc.
     */
    String getTypeName();

}
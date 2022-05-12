package com.github.imthenico.annihilation.api.game;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.game.PlayerJoinRoomEvent;
import com.github.imthenico.annihilation.api.event.game.PlayerLeaveRoomEvent;
import com.github.imthenico.annihilation.api.match.expansion.MatchExpansion;
import com.github.imthenico.annihilation.api.model.lobby.GameLobby;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchFactory;
import com.github.imthenico.annihilation.api.match.authorization.MatchAuthorizer;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.strategy.MatchMapModelProvider;
import com.github.imthenico.annihilation.api.validator.MapCandidateValidator;
import com.github.imthenico.annihilation.api.world.LocationReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class SimpleGameRoom implements GameRoom {

    private final String typeName;
    private final LocationReference lobbySpawn;
    private final Set<AnniPlayer> players;
    private final GameLobby gameLobby;
    private final String id;
    private final Rules rules;
    private final Options options;
    private final MatchAuthorizer matchAuthorizer;
    private final MatchFactory matchFactory;
    private final MatchExpansion matchExpansion;
    private final MatchMapModelProvider matchMapModelProvider;
    private final MapCandidateValidator mapCandidateValidator;

    private Game game;
    private boolean enabled;

    public SimpleGameRoom(
            GameLobby gameLobby,
            String typeName,
            LocationReference lobbySpawn,
            String id,
            Rules rules,
            Options options,
            MatchAuthorizer matchAuthorizer,
            MatchFactory matchFactory,
            MatchExpansion matchExpansion,
            MatchMapModelProvider matchMapModelProvider,
            MapCandidateValidator mapCandidateValidator
    ) {
        this.matchExpansion = matchExpansion;
        this.players = new HashSet<>();
        this.gameLobby = gameLobby;
        this.typeName = typeName;
        this.lobbySpawn = lobbySpawn;
        this.id = id;
        this.rules = rules;
        this.options = options;
        this.matchAuthorizer = matchAuthorizer;
        this.matchFactory = matchFactory;
        this.matchMapModelProvider = matchMapModelProvider;
        this.mapCandidateValidator = mapCandidateValidator;

        initLogic();
    }

    @Override
    public GameLobby getLobby() {
        return gameLobby;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (!enabled) {
            Match match = game.runningMatch();

            if (match != null) {
                match.terminate();
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Rules getRules() {
        return rules;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public MatchAuthorizer getMatchAuthorizer() {
        return matchAuthorizer;
    }

    @Override
    public Game game() {
        return game;
    }

    @Override
    public void restoreLogic() {
        if (!enabled)
            throw new IllegalStateException("Room is disabled");

        for (AnniPlayer player : getPlayers((p) -> true)) {
            player.getPlayer().teleport(lobbySpawn.get());
        }

        Match match = game.runningMatch();

        if (match != null) {
            match.terminate();
        }

        initLogic();
    }

    @Override
    public void join(AnniPlayer anniPlayer) {
        GameRoom room = anniPlayer.getPlayingRoom();

        if (room != null) {
            if (!equals(room)) {
                throw new IllegalArgumentException("Player is playing in another room");
            }
        }

        PlayerJoinRoomEvent event = new PlayerJoinRoomEvent(this, anniPlayer);

        if (event.isCancelled()) {
            return;
        }

        this.players.add(anniPlayer);
        anniPlayer.handleInternalGameJoin(this);

        Match match = game.runningMatch();

        if (match != null) {
            MatchPlayer matchPlayer = match.getPlayer(anniPlayer);

            if (matchPlayer != null && !matchPlayer.isDisqualified()) {
                return;
            }
        }

        Player player = anniPlayer.getPlayer();
        player.teleport(gameLobby.getSpawn());
    }

    @Override
    public void leave(AnniPlayer anniPlayer) {
        if (!isWithin(anniPlayer)) {
            throw new IllegalArgumentException("That player is not registered in this room");
        }

        this.players.remove(anniPlayer);
        anniPlayer.handleInternalGameLeave(this);

        anniPlayer.getPlayer().teleport(lobbySpawn.get());

        Match match = game.runningMatch();

        if (match != null && isWithin(anniPlayer)) {
            MatchPlayer matchPlayer = match.getPlayer(anniPlayer);

            if (matchPlayer != null)
                match.handleLeave(matchPlayer);
        }

        Bukkit.getPluginManager().callEvent(new PlayerLeaveRoomEvent(this, anniPlayer));
    }

    @Override
    public boolean isWithin(AnniPlayer anniPlayer) {
        return players.contains(anniPlayer);
    }

    @Override
    public List<AnniPlayer> getPlayers(Predicate<AnniPlayer> filter) {
        List<AnniPlayer> anniPlayers = new ArrayList<>(players.size());

        for (AnniPlayer player : players) {
            if (filter.test(player))
                anniPlayers.add(player);
        }

        return anniPlayers;
    }

    @Override
    public int playerCount() {
        return players.size();
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    private void initLogic() {
        this.game = new Game(
                this,
                matchFactory,
                matchExpansion,
                matchAuthorizer,
                matchMapModelProvider,
                mapCandidateValidator,
                rules.getTimeToStart()
        );
    }
}
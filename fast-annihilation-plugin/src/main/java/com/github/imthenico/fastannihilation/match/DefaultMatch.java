package com.github.imthenico.fastannihilation.match;

import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.event.match.PlayerJoinMatchEvent;
import com.github.imthenico.annihilation.api.event.match.PlayerLeaveMatchEvent;
import com.github.imthenico.annihilation.api.event.match.MatchEndEvent;
import com.github.imthenico.annihilation.api.event.match.MatchFinalizeEvent;
import com.github.imthenico.annihilation.api.event.match.MatchStartEvent;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.PreMatchStage;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.match.MatchClosingStage;
import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.*;
import java.util.function.Predicate;

public class DefaultMatch implements Match {

    private final Set<Object> disqualifiedPlayers;
    private final Map<UUID, MatchPlayer> players;

    private final MatchMap runningMap;
    private final Game game;
    private final PhaseManager phaseManager;
    private boolean finalized;
    private final MatchClosingStage ending;
    private final PlayerSetup playerSetup;
    private boolean started;

    DefaultMatch(
            MatchMap runningMap,
            Game game,
            PhaseManager phaseManager,
            MatchClosingStage ending,
            PlayerSetup playerSetup
    ) {
        this.runningMap = runningMap;

        this.game = game;
        this.phaseManager = phaseManager;

        ending.setMatch(this);

        this.ending = new UnmodifiableClosingStage(ending);
        this.disqualifiedPlayers = new HashSet<>();
        this.players = new HashMap<>();
        this.playerSetup = playerSetup;
    }

    @Override
    public MatchMap getRunningMap() {
        return runningMap;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }

    @Override
    public PlayerSetup getPlayerSetup() {
        return playerSetup;
    }

    @Override
    public MatchPlayer getPlayer(AnniPlayer player) {
        return players.get(player.getId());
    }

    @Override
    public void setDisqualified(UUID uuid, boolean disqualified) {
        if (disqualified) {
            disqualifiedPlayers.add(uuid);
        } else {
            disqualifiedPlayers.remove(uuid);
        }
    }

    @Override
    public boolean isDisqualified(UUID uuid) {
        return disqualifiedPlayers.contains(uuid);
    }

    @Override
    public void handleLeave(MatchPlayer matchPlayer) {
        if (!players.containsKey(matchPlayer.getUniqueId()) || !equals(matchPlayer.getMatch()))
            throw new IllegalArgumentException("Invalid match player");

        Bukkit.getPluginManager().callEvent(new PlayerLeaveMatchEvent(matchPlayer));
    }

    @Override
    public MatchPlayer handleJoin(AnniPlayer player) {
        if (game.room().isWithin(player)) {
            throw new IllegalStateException("This player is already playing this match");
        }

        MatchPlayer matchPlayer = getPlayer(player);

        if (matchPlayer == null) {
            matchPlayer = new MatchPlayer(player, this);

            this.players.put(player.getId(), matchPlayer);

            Bukkit.getPluginManager().callEvent(new PlayerJoinMatchEvent(matchPlayer));
        }

        return matchPlayer;
    }

    @Override
    public boolean finalized() {
        return finalized;
    }

    @Override
    public MatchClosingStage getClosingStage() {
        return ending;
    }

    @Override
    public void terminate() throws UnsupportedOperationException {
        checkState();

        this.finalized = true;

        Bukkit.getPluginManager().callEvent(new MatchFinalizeEvent(this));

        runningMap.allWorlds().forEach(world -> Bukkit.unloadWorld((World) world.getHandle(), false));
    }

    @Override
    public boolean allPhasesFinished() {
        return phaseManager.isLastPhase() && phaseManager.getCurrentPhase().finished();
    }

    @Override
    public void start() throws UnsupportedOperationException {
        if (started)
            throw new UnsupportedOperationException("Match is already running");

        this.started = true;

        PreMatchStage preMatchStage = game.getPreparationStage();

        Map<UUID, TeamColor> teamSelection = preMatchStage.getTeamSelection();

        for (AnniPlayer player : game.room().getPlayers((p) -> true)) {
            TeamColor color = teamSelection.get(player.getId());

            if (color == null)
                continue;

            MatchPlayer matchPlayer = handleJoin(player);

            MatchTeam team = runningMap.getTeam(color);
            team.join(matchPlayer);
        }

        Bukkit.getPluginManager().callEvent(new MatchStartEvent(this));
    }

    @Override
    public void end() {
        Bukkit.getPluginManager().callEvent(new MatchEndEvent(this));

        ending.start();
    }

    @Override
    public SimpleTimer getActiveTimer() {
        if (ending.isRunning())
            return ending.getTimer();

        return phaseManager.getTimer();
    }

    @Override
    public Collection<MatchPlayer> getPlayers(Predicate<MatchPlayer> filter) {
        List<MatchPlayer> matchPlayers = new ArrayList<>();

        for (MatchPlayer matchPlayer : players.values()) {
            if (filter.test(matchPlayer))
                matchPlayers.add(matchPlayer);
        }

        return matchPlayers;
    }

    @Override
    public String getTypeName() {
        return game.getTypeName();
    }

    private void checkState() {
        if (finalized)
            throw new UnsupportedOperationException("Match is finalized.");
    }
}
package com.github.imthenico.annihilation.api.match;

import com.github.imthenico.annihilation.api.entity.EntityId;
import com.github.imthenico.annihilation.api.entity.UUIDEntityId;
import com.github.imthenico.annihilation.api.event.MatchFinalizeEvent;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.ingame.MatchMap;
import com.github.imthenico.annihilation.api.entity.MatchPlayer;
import com.github.imthenico.annihilation.api.phase.PhaseManager;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerEventHandler;
import com.github.imthenico.annihilation.api.player.PlayerSetup;
import com.github.imthenico.annihilation.api.scheduler.Scheduler;
import com.github.imthenico.annihilation.api.util.SimpleTimer;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.Bukkit;

import java.util.*;
import java.util.function.Predicate;

public class DefaultMatch implements Match {

    private final Set<Object> disqualifiedPlayers;
    private final String typeName;

    private MatchMap runningMap;
    private MatchEventHandler eventHandler;
    private GameInstance game;
    private PhaseManager phaseManager;
    private boolean finalized;
    private MatchClosingStage ending;
    private PlayerSetup playerSetup;
    private PlayerEventHandler playerEventHandler;
    private boolean started;
    private Scheduler scheduler;

    public DefaultMatch(
            String typeName,
            MatchMap runningMap,
            MatchEventHandler eventHandler,
            GameInstance game,
            PhaseManager phaseManager,
            MatchClosingStage ending,
            PlayerSetup playerSetup,
            PlayerEventHandler playerEventHandler,
            Scheduler scheduler
    ) {
        this.typeName = typeName;
        this.runningMap = runningMap;
        this.eventHandler = eventHandler;
        this.game = game;
        this.phaseManager = phaseManager;

        ending.setMatch(this);

        this.ending = new UnmodifiableClosingStage(ending);
        this.disqualifiedPlayers = new HashSet<>();
        this.playerSetup = playerSetup;
        this.playerEventHandler = playerEventHandler;
        this.scheduler = scheduler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MatchMap getRunningMap() {
        return runningMap;
    }

    @Override
    public MatchEventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
    public GameInstance getGame() {
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
    public PlayerEventHandler getPlayerEventHandler() {
        return playerEventHandler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public MatchPlayer getPlayer(AnniPlayer player) {
        return runningMap.getEntity(new UUIDEntityId(player.getId())).orDefault(null);
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
    public void leave(AnniPlayer player) {
        Validate.isTrue(game.isInGame(player), "This player is not playing this game.");

        MatchPlayer matchPlayer = getPlayer(player);

        if (matchPlayer != null) {
            playerEventHandler.handleLeave(matchPlayer);
        }
    }

    @Override
    public void join(AnniPlayer player) {
        Validate.isTrue(game.isInGame(player), "This player is not playing this game.");

        EntityId entityId = new UUIDEntityId(player.getId());

        MatchPlayer matchPlayer = getPlayer(player);

        if (matchPlayer != null) {
            Validate.isTrue(!matchPlayer.isDisqualified(), "Player is disqualified");
        } else {
            matchPlayer = new MatchPlayer(player, this);

            runningMap.addEntity(entityId, matchPlayer);
        }

        playerEventHandler.handleJoin(matchPlayer);
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

        runningMap.getWorlds().values().forEach(simpleWorld -> Bukkit.unloadWorld(simpleWorld.getWorld(), false));
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

        eventHandler.handleMatchStart(this);
    }

    @Override
    public void end() {
        eventHandler.handleMatchEnd(getState());

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

        for (EntityId entityId : runningMap.getEntitiesId()) {
            MatchPlayer matchPlayer = runningMap.getEntity(entityId).orDefault(null);

            if (matchPlayer != null && filter.test(matchPlayer)) {
                matchPlayers.add(matchPlayer);
            }
        }

        return matchPlayers;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    private void checkState() {
        if (finalized)
            throw new UnsupportedOperationException("Match is finalized.");
    }
}
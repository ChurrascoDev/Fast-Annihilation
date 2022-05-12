package com.github.imthenico.annihilation.api.entity;

import com.github.imthenico.annihilation.api.equipment.Kit;
import com.github.imthenico.annihilation.api.game.Game;
import com.github.imthenico.annihilation.api.game.GameRoom;
import com.github.imthenico.annihilation.api.lang.LangHolder;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class MatchPlayer implements LangHolder {

    private final AnniPlayer rootPlayer;
    private final Match match;

    private MatchTeam team;
    private Kit kit;

    public MatchPlayer(
            AnniPlayer rootPlayer,
            Match match
    ) {
        this.rootPlayer = Objects.requireNonNull(rootPlayer);
        this.match = Objects.requireNonNull(match);
    }

    public static MatchPlayer from(AnniPlayer anniPlayer) {
        GameRoom gameRoom = anniPlayer.getPlayingRoom();

        if (gameRoom == null || !gameRoom.isEnabled())
            return null;

        Game game = gameRoom.game();

        Match match = game.runningMatch();
        if (match != null) {
            return match.getPlayer(anniPlayer);
        }

        return null;
    }

    public Match getMatch() {
        return match;
    }

    public Player getPlayer() {
        return rootPlayer.getPlayer();
    }

    public AnniPlayer getHandle() {
        return rootPlayer;
    }

    public MatchTeam getTeam() {
        return team;
    }

    public boolean isPlaying() {
        GameRoom gameRoom = getMatch().getGame().room();

        return gameRoom.isWithin(rootPlayer) && team != null && team.isDeath();
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public boolean isDisqualified() {
        return match.isDisqualified(getUniqueId());
    }

    public void setDisqualified(boolean disqualified) {
        match.setDisqualified(getUniqueId(), disqualified);
    }

    public void handleTeamJoin(MatchTeam team) {
        if (team != null) {
            if (team.equals(this.team))
                throw new IllegalArgumentException("This player is already in the team");

            if (!team.isMember(this))
                throw new IllegalArgumentException("Invalid team or player is not member");
        }

        this.team = team;
    }

    public void handleTeamLeave(MatchTeam team) {
        if (this.team == null)
            return;

        if (!this.team.equals(team)) {
            throw new IllegalArgumentException("The player is not in the team");
        }

        if (team.isMember(this)) {
            throw new IllegalStateException("Player is still in the team");
        }

        this.team = null;
    }

    public UUID getUniqueId() {
        return rootPlayer.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchPlayer that = (MatchPlayer) o;
        return rootPlayer.equals(that.rootPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootPlayer);
    }

    public boolean isSpawned() {
        return !isDeath();
    }

    private boolean isDeath() {
        return rootPlayer.getPlayer().getHealth() <= 0;
    }

    @Override
    public @Nullable String getLang() {
        return rootPlayer.getLang();
    }

}
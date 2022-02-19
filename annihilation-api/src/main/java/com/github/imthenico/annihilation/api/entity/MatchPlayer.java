package com.github.imthenico.annihilation.api.entity;

import com.github.imthenico.annihilation.api.equipment.Kit;
import com.github.imthenico.annihilation.api.game.GameInstance;
import com.github.imthenico.annihilation.api.lang.LangHolder;
import com.github.imthenico.annihilation.api.match.Match;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.MatchTeam;
import com.github.imthenico.simplecommons.util.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class MatchPlayer implements Entity<AnniPlayer>, Spawnable, LangHolder {

    private final AnniPlayer rootPlayer;
    private final Match match;

    private MatchTeam team;
    private Kit kit;

    public MatchPlayer(
            AnniPlayer rootPlayer,
            Match match
    ) {
        this.rootPlayer = Validate.notNull(rootPlayer);
        this.match = Validate.notNull(match);
    }

    public Match getMatch() {
        return match;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AnniPlayer getHandle() {
        return rootPlayer;
    }

    public MatchTeam getTeam() {
        return team;
    }

    public boolean isPlaying() {
        GameInstance gameInstance = getMatch().getGame();

        return gameInstance.isInGame(rootPlayer) && team != null && team.isDeath();
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
        Validate.isTrue(team != null && team.isMember(this), "Invalid team or player is not member.");
        Validate.isTrue(!team.equals(this.team), "This player is already in that team.");

        this.team = team;
    }

    public void handleTeamLeave(MatchTeam team) {
        Validate.isTrue(Validate.notNull(team).equals(this.team) && !team.isMember(this), "Invalid team or player is member.");

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

    @Override
    public void spawn() {
        Validate.notNull(team, "The player isn't on any team.");

        Player player = rootPlayer.getPlayer();

        if (isDeath()) {
            player.spigot().respawn();
        }
    }

    @Override
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
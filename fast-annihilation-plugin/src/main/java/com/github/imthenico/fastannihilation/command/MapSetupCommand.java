package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.editor.SetupContext;
import com.github.imthenico.annihilation.api.editor.ModelSetupManager;
import com.github.imthenico.annihilation.api.model.LocationModel;
import com.github.imthenico.annihilation.api.model.NexusModel;
import com.github.imthenico.annihilation.api.model.TeamDataModel;
import com.github.imthenico.annihilation.api.model.map.data.MatchMapData;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.gmlib.ModelData;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.fixeddev.commandflow.exception.CommandException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;

@Command(names = "mapeditor")
public class MapSetupCommand implements CommandClass {

    private final ModelSetupManager modelSetupManager;

    public MapSetupCommand(
            ModelSetupManager modelSetupManager
    ) {
        this.modelSetupManager = modelSetupManager;
    }

    @Command(names = "setspawn")
    public boolean setTeamSpawn(
            @Sender Player player,
            TeamColor color
    ) {
        SetupContext<MatchMapData> context = getSetupContext(player);

        if (context == null)
            return true;

        MatchMapData changesProduced = context.getChangesProduced();

        TeamDataModel dataModel = changesProduced.getTeamData(color);

        LocationModel locationModel = normalizePosition(player);

        dataModel.addSpawn(locationModel);

        player.sendMessage(
                color.getColorCode() +
                color.name() +
                Formatting.formatLocation(locationModel, 1, "spawn placed at %s, %s, %s")
        );

        return true;
    }

    @Command(names = "setspectatorpos")
    public boolean setSpectatorPos(
            @Sender Player player,
            TeamColor color
    ) {
        SetupContext<MatchMapData> context = getSetupContext(player);

        if (context == null)
            return true;

        MatchMapData changesProduced = context.getChangesProduced();

        TeamDataModel dataModel = changesProduced.getTeamData(color);

        LocationModel locationModel = normalizePosition(player);

        dataModel.addSpectatorPos(locationModel);

        player.sendMessage(
                color.getColorCode() +
                        color.name() +
                        Formatting.formatLocation(locationModel, 1, "spectator position placed at %s, %s, %s")
        );

        return true;
    }

    @Command(names = "setnexus")
    public boolean setupNexus(
            @Sender Player player,
            TeamColor color,
            int health
    ) {
        SetupContext<MatchMapData> context = getSetupContext(player);

        if (context == null)
            return true;

        Block facingBlock = player.getTargetBlock(
                (Set<Material>) null,
                5
        );

        if (!facingBlock.getType().isSolid()) {
            throw new CommandException(String.format("Invalid block target type (%s)", facingBlock.getType()));
        }

        Location location = facingBlock.getLocation();

        MatchMapData changesProduced = context.getChangesProduced();

        TeamDataModel dataModel = changesProduced.getTeamData(color);

        dataModel.setNexus(new NexusModel(health, LocationModel.fromBukkit(location)));

        player.sendMessage(color.getColoredName() + " nexus settled correctly");

        return true;
    }

    @SuppressWarnings("unchecked")
    private SetupContext<MatchMapData> getSetupContext(Player player) {
        SetupContext<?> setupContext = modelSetupManager.getSession(player);

        if (setupContext == null) {
            player.sendMessage("You're not editing any map.");
        } else {
            ModelData data = setupContext.getEditingTarget().getData();

            if (!(data instanceof MatchMapData)) {
                player.sendMessage("This map is not a match map model");
            }
        }

        return (SetupContext<MatchMapData>) setupContext;
    }

    private LocationModel normalizePosition(Player player) {
        return LocationModel.fromBukkit(player
                .getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5));
    }
}
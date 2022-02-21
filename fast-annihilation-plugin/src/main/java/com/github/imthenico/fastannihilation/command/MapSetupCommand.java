package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.cache.ConfigurableModelCache;
import com.github.imthenico.annihilation.api.editor.SetupContext;
import com.github.imthenico.annihilation.api.editor.SetupManager;
import com.github.imthenico.annihilation.api.map.model.NexusModel;
import com.github.imthenico.annihilation.api.model.ConfigurableModel;
import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKeys;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.simplecommons.bukkit.util.Conversions;
import com.github.imthenico.simplecommons.minecraft.LocationModel;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.fixeddev.commandflow.exception.CommandException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.github.imthenico.annihilation.api.util.MapPropertiesHelper.*;

@Command(names = "mapeditor")
public class MapSetupCommand implements CommandClass {

    private final SetupManager setupManager;
    private final ConfigurableModelCache configurableModelCache;

    public MapSetupCommand(
            SetupManager setupManager,
            ConfigurableModelCache configurableModelCache
    ) {
        this.setupManager = setupManager;
        this.configurableModelCache = configurableModelCache;
    }

    @Command(names = "editmap")
    public boolean setupMap(@Sender AnniPlayer anniPlayer, String mapName) {
        ConfigurableModel mapModel = configurableModelCache.getModel(mapName);
        Player player = anniPlayer.getPlayer();

        if (mapModel == null) {
            player.sendMessage("Invalid map name");
            return true;
        }

        try {
            setupManager.setupMap(anniPlayer, mapModel);
            World world = mapModel.getMainWorld().getWorld();

            player.teleport(world.getSpawnLocation());

            player.sendMessage("You're now editing " + mapModel.getId());
        } catch (UnsupportedOperationException e) {
            player.sendMessage("You're already editing a map");
        }

        return true;
    }

    @Command(names = "setspawn")
    public boolean setTeamSpawn(
            @Sender Player player,
            TeamColor color
    ) {
        SetupContext context = getSetupContext(player);

        if (context == null)
            return true;

        PropertiesContainer propertiesContainer = context.getChangesProduced();

        List<LocationModel> spawns = propertiesContainer.getProperty(PropertyKeys.teamSpawns(color))
                .orDefault(ArrayList::new);

        LocationModel locationModel = Conversions.fromBukkitLocation(player
                .getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5));

        spawns.add(locationModel);

        player.sendMessage(
                color.getColorCode() +
                color.name() +
                Formatting.formatLocation(locationModel, 1, "spawn placed at %s, %s, %s")
        );

        return true;
    }

    @Command(names = "setnexus")
    public boolean setupNexus(
            @Sender Player player,
            TeamColor teamColor,
            int health
    ) {
        SetupContext context = getSetupContext(player);

        if (context == null)
            return true;

        Block facingBlock = player.getTargetBlock(
                null,
                5
        );

        if (!facingBlock.getType().isSolid()) {
            throw new CommandException(String.format("Invalid block target type (%s)", facingBlock.getType()));
        }

        Location location = facingBlock.getLocation();

        setNexusModel(
                teamColor,
                new NexusModel(health, Conversions.fromBukkitLocation(location)),
                context.getChangesProduced()
        );

        player.sendMessage(teamColor.getColorCode() + teamColor.name() + " nexus settled correctly");

        return true;
    }

    private SetupContext getSetupContext(Player player) {
        SetupContext setupContext = setupManager.getSession(player);

        if (setupContext == null) {
            player.sendMessage("You're not editing any maps.");
        }

        return setupContext;
    }
}
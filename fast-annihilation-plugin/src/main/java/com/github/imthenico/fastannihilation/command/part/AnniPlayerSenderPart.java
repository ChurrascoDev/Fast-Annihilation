package com.github.imthenico.fastannihilation.command.part;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.TranslatableComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class AnniPlayerSenderPart implements CommandPart {

    private final String name;
    private final PlayerRegistry playerRegistry;

    public AnniPlayerSenderPart(String name, PlayerRegistry playerRegistry) {
        this.name = name;
        this.playerRegistry = playerRegistry;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, @Nullable CommandPart caller) throws ArgumentParseException {
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE);

        if (sender == null)
            throw new ArgumentParseException(TranslatableComponent.of("sender.unknown"));

        if (!(sender instanceof Player))
            throw new ArgumentParseException(TranslatableComponent.of("sender.only-player"));

        Player player = (Player) sender;

        AnniPlayer anniPlayer = playerRegistry.getPlayer(player.getUniqueId());

        if (anniPlayer != null) {
            context.setValue(this, anniPlayer);
        }
    }
}
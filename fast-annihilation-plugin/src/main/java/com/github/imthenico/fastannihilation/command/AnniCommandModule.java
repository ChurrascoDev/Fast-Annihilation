package com.github.imthenico.fastannihilation.command;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.annihilation.api.player.PlayerRegistry;
import com.github.imthenico.annihilation.api.team.TeamColor;
import com.github.imthenico.fastannihilation.command.part.AnniPlayerSenderPart;
import com.github.imthenico.fastannihilation.command.part.TeamColorPart;
import me.fixeddev.commandflow.annotated.part.Key;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;

public class AnniCommandModule extends BukkitModule {

    private final PlayerRegistry playerRegistry;

    public AnniCommandModule(PlayerRegistry playerRegistry) {
        this.playerRegistry = playerRegistry;
    }

    @Override
    public void configure() {
        super.configure();

        bindFactory(new Key(AnniPlayer.class, Sender.class),
                (s, list) -> new AnniPlayerSenderPart(s, playerRegistry));

        bindFactory(TeamColor.class, (s, list) -> new TeamColorPart(s));
    }
}
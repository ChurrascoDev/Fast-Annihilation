package com.github.imthenico.fastannihilation.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

@Command(names = "vote")
public class VoteCommand implements CommandClass {

    @Command(names = "")
    public boolean rootCommand(@Sender Player player, String mapName) {
        return true;
    }
}
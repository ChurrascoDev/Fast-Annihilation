package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.message.AbstractMessage;
import com.github.imthenico.annihilation.api.util.Formatting;
import com.github.imthenico.annihilation.api.util.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;

public class SimpleAnniMessageSender implements AnniMessageSender<Player> {
    @Override
    public void sendAbstractMessage(
            Player receiver,
            String mode,
            AbstractMessage<?> message
    ) {
        Object handle = message.getMessage();

        if (handle instanceof Title) {
            // send it as title
        } else if (handle instanceof BaseComponent) {
            receiver.spigot().sendMessage((BaseComponent) handle);
        } else if (handle instanceof String) {
            send(receiver, mode, (String) handle);
        }
    }

    @Override
    public void send(Player player, String mode, String s1) {
        if ("action-bar".equalsIgnoreCase(mode)) {
            // send message as action bar
        } else {
            player.sendMessage(Formatting.colorize(s1));
        }
    }
}
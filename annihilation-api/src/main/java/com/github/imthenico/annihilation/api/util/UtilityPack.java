package com.github.imthenico.annihilation.api.util;

import java.util.Objects;

import com.github.imthenico.annihilation.api.lang.CustomMessageHandler;
import me.yushust.message.MessageHandler;

public class UtilityPack {

    private final CustomMessageHandler messageHandler;

    public UtilityPack(MessageHandler messageHandler) {
        this.messageHandler = new CustomMessageHandler(Objects.requireNonNull(messageHandler));
    }

    public CustomMessageHandler getMessageHandler() {
        return messageHandler;
    }
}
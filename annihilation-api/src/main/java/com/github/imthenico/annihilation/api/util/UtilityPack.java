package com.github.imthenico.annihilation.api.util;

import java.util.Objects;
import me.yushust.message.MessageHandler;

public class UtilityPack {

    private final MessageHandler messageHandler;

    public UtilityPack(MessageHandler messageHandler) {
        this.messageHandler = Objects.requireNonNull(messageHandler);
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
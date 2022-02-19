package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.simplecommons.util.Validate;
import me.yushust.message.MessageHandler;

public class UtilityPack {

    private final MessageHandler messageHandler;

    public UtilityPack(MessageHandler messageHandler) {
        this.messageHandler = Validate.notNull(messageHandler);
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }
}
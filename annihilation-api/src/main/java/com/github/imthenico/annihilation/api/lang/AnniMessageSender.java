package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.message.ReplaceableMessage;
import me.yushust.message.send.MessageSender;

public interface AnniMessageSender<T> extends MessageSender<T> {

    void sendAbstractMessage(
            T receiver,
            String mode,
            ReplaceableMessage<?> message
    );
}
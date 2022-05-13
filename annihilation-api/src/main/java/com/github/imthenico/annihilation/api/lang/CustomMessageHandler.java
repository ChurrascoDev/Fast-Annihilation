package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.message.AbstractMessage;
import com.github.imthenico.annihilation.api.message.MessagePath;
import me.yushust.message.MessageHandler;
import me.yushust.message.impl.AbstractDelegatingMessageProvider;
import me.yushust.message.send.MessageSender;
import me.yushust.message.util.ReplacePack;

public class CustomMessageHandler extends AbstractDelegatingMessageProvider {

    private final MessageHandler delegate;

    public CustomMessageHandler(MessageHandler delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    public void dispatch(
            Object entityOrEntities,
            String path,
            String mode,
            ReplacePack replacePack,
            Object... objects
    ) {
        dispatch(objects, path, mode, null, replacePack);
    }

    @SuppressWarnings("rawtypes, unchecked")
    public void dispatch(
            Object entityOrEntities,
            String path,
            String mode,
            Object defaultMessage,
            ReplacePack replacePack
    ) {
        if (entityOrEntities instanceof Iterable) {
            for (Object entity : ((Iterable<?>) entityOrEntities)) {
                dispatch(entity, path, mode, replacePack);
            }
        } else {
            MessageSender<?> messageSender = config.getSender(entityOrEntities.getClass());

            if (messageSender instanceof AnniMessageSender) {
                AnniMessageSender anniMessageSender = (AnniMessageSender) messageSender;

                String lang = getLanguage(entityOrEntities);
                Object found = source.get(lang, path);

                Object rawMessage = found != null ? found : (defaultMessage != null ? defaultMessage : path);

                AbstractMessage<?> message = AbstractMessage.of(rawMessage);

                message.applyReplacements(replacePack);

                anniMessageSender.sendAbstractMessage(
                        entityOrEntities,
                        mode,
                        message
                );
            } else {
                delegate.dispatch(entityOrEntities, path, mode, replacePack);
            }
        }
    }

    public void dispatch(
            Object entityOrEntities,
            String mode,
            MessagePath messagePath,
            ReplacePack replacePack
    ) {
        dispatch(entityOrEntities, messagePath.getMessagePath(), mode, messagePath.getDefaultMessage(), replacePack);
    }
}
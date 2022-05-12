package com.github.imthenico.annihilation.api.lang;

import com.github.imthenico.annihilation.api.message.AbstractMessage;
import me.yushust.message.impl.AbstractDelegatingMessageProvider;
import me.yushust.message.send.MessageSender;
import me.yushust.message.send.impl.MessageHandlerImpl;
import me.yushust.message.util.ReplacePack;

import java.util.Objects;

public class CustomMessageHandler extends AbstractDelegatingMessageProvider {

    private final MessageHandlerImpl delegate;

    public CustomMessageHandler(MessageHandlerImpl delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("rawtypes, unchecked")
    public void dispatch(
            Object entityOrEntities,
            String path,
            String mode,
            ReplacePack replacePack,
            Object... objects
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

                if (Objects.equals(path, found))
                    return;

                AbstractMessage<?> message = AbstractMessage.of(found);

                message.applyReplacements(replacePack);

                anniMessageSender.sendAbstractMessage(
                        entityOrEntities,
                        mode,
                        message
                );
            } else {
                delegate.dispatch(entityOrEntities, path, mode, replacePack, objects);
            }
        }
    }
}
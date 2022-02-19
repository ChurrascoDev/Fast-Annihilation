package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.annihilation.api.lang.LangHolder;
import com.github.imthenico.annihilation.api.team.TeamColor;
import me.yushust.message.MessageHandler;

import java.util.Locale;

public interface Translations {

    static String getColorTranslation(
            MessageHandler messageHandler,
            LangHolder langHolder,
            TeamColor color
    ) {
        return getTranslation(messageHandler, langHolder, "colors." + color.name().toLowerCase(Locale.ROOT));
    }

    static String getTranslation(
            MessageHandler messageHandler,
            LangHolder langHolder,
            String path
    ) {
        Object message = messageHandler.getSource().get(langHolder.getLang(), path);

        if (!(message instanceof String))
            return messageHandler.getSource().get("default", path).toString();

        return (String) message;
    }
}
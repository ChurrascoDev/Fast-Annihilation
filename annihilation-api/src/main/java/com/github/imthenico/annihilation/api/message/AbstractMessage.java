package com.github.imthenico.annihilation.api.message;

import com.github.imthenico.annihilation.api.util.Title;
import me.yushust.message.util.ReplacePack;
import net.md_5.bungee.api.chat.BaseComponent;

public interface AbstractMessage<T> {

    void applyReplacements(ReplacePack replacePack);

    T getMessage();

    static AbstractMessage<?> of(Object obj) {
        if (obj instanceof Title) {
            return newTitleMessage((Title) obj);
        } else if (obj instanceof BaseComponent) {
            return newComponentMessage((BaseComponent) obj);
        } else if (obj instanceof String) {
            return common((String) obj);
        }

        throw new IllegalArgumentException(String.format("Invalid message type '%s'", obj.getClass().getName()));
    }

    static AbstractMessage<String> common(String str) {
        return new SimpleMessage(str);
    }

    static AbstractMessage<BaseComponent> newComponentMessage(BaseComponent baseComponent) {
        return new ComponentMessage(baseComponent);
    }

    static AbstractMessage<Title> newTitleMessage(Title title) {
        return new TitleMessage(title);
    }
}
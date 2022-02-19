package com.github.imthenico.annihilation.api.message;

import com.github.imthenico.simplecommons.minecraft.Title;
import me.yushust.message.util.ReplacePack;
import net.md_5.bungee.api.chat.BaseComponent;

public interface ReplaceableMessage<T> {

    void applyReplacements(ReplacePack replacePack);

    T getMessage();

    static ReplaceableMessage<?> of(Object obj) {
        if (obj instanceof Title) {
            return newTitleMessage((Title) obj);
        } else if (obj instanceof BaseComponent) {
            return newComponentMessage((BaseComponent) obj);
        } else if (obj instanceof String) {
            return common((String) obj);
        }

        throw new IllegalArgumentException(String.format("Invalid message type '%s'", obj.getClass().getName()));
    }

    static ReplaceableMessage<String> common(String str) {
        return new SimpleMessage(str);
    }

    static ReplaceableMessage<BaseComponent> newComponentMessage(BaseComponent baseComponent) {
        return new ComponentMessage(baseComponent);
    }

    static ReplaceableMessage<Title> newTitleMessage(Title title) {
        return new TitleMessage(title);
    }
}
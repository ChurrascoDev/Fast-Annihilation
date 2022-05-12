package com.github.imthenico.annihilation.api.message;

import me.yushust.message.util.ReplacePack;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentMessage implements AbstractMessage<BaseComponent> {

    private BaseComponent baseComponent;

    ComponentMessage(BaseComponent baseComponent) {
        this.baseComponent = baseComponent;
    }

    @Override
    public void applyReplacements(ReplacePack replacePack) {
        String legacy = replacePack.replace(baseComponent.toLegacyText());

        this.baseComponent = new TextComponent(legacy);
    }

    @Override
    public BaseComponent getMessage() {
        return baseComponent;
    }
}
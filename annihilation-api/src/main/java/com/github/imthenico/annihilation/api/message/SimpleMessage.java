package com.github.imthenico.annihilation.api.message;

import me.yushust.message.util.ReplacePack;

public class SimpleMessage implements AbstractMessage<String> {

    private String string;

    SimpleMessage(String string) {
        this.string = string;
    }

    @Override
    public void applyReplacements(ReplacePack replacePack) {
        this.string = replacePack.replace(string);
    }

    @Override
    public String getMessage() {
        return string;
    }
}
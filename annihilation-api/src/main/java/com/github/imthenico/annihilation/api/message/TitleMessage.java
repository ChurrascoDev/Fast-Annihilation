package com.github.imthenico.annihilation.api.message;

import com.github.imthenico.annihilation.api.util.Title;
import me.yushust.message.util.ReplacePack;

public class TitleMessage implements AbstractMessage<Title> {

    private Title title;

    TitleMessage(Title title) {
        this.title = title;
    }

    @Override
    public void applyReplacements(ReplacePack replacePack) {
        String title = replacePack.replace(this.title.getTitle());
        String subTitle = replacePack.replace(this.title.getSubTitle());

        this.title = new Title(title, subTitle, this.title.getFadeIn(), this.title.getStay(), this.title.getFadeOut());
    }

    @Override
    public Title getMessage() {
        return title;
    }
}
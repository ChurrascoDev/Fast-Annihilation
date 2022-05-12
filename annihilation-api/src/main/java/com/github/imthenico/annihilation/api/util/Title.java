package com.github.imthenico.annihilation.api.util;

import com.github.imthenico.json.JsonSerializable;
import com.github.imthenico.json.JsonTreeBuilder;
import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public class Title implements JsonSerializable {

    private final String title;
    private final String subTitle;
    private final int fadeIn;
    private final int stay;
    private final int fadeOut;

    public Title(
            String title, String subTitle
    ) {
        this(title, subTitle, 1, 1, 1);
    }

    public Title(
            String title, String subTitle, int fadeIn, int stay, int fadeOut
    ) {
        this.title = title != null ? title : "";
        this.subTitle = subTitle != null ? subTitle : "";
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getStay() {
        return stay;
    }

    public int getFadeOut() {
        return fadeOut;
    }

    @Override
    public @NotNull JsonElement serialize() {
        JsonTreeBuilder builder = JsonTreeBuilder.create();

        return builder
                .add("title", title)
                .add("subTitle", subTitle)
                .add("fadeIn", fadeIn)
                .add("stay", stay)
                .add("fadeOut", fadeOut)
                .build();
    }
}
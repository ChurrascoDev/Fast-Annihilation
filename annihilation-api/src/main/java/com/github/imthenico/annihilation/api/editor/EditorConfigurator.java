package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;
import com.grinderwolf.swm.api.world.SlimeWorld;

public interface EditorConfigurator {

    void configure(
        AnniPlayer editor,
        MapModel<?> model
    );
}
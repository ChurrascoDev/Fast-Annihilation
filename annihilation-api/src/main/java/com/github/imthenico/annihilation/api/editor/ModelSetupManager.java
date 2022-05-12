package com.github.imthenico.annihilation.api.editor;

import com.github.imthenico.annihilation.api.model.EditableMapData;
import com.github.imthenico.annihilation.api.player.AnniPlayer;
import com.github.imthenico.gmlib.MapModel;
import org.bukkit.entity.Player;

public interface ModelSetupManager {

    <T extends EditableMapData> SetupContext<T> getSession(MapModel<?> model);

    <T extends EditableMapData> SetupContext<T> getSession(Player player);

    <T extends EditableMapData> SetupContext<T> removePlayerFromSession(AnniPlayer player);

    default <T extends EditableMapData> SetupContext<T> getSession(AnniPlayer player) {
        return getSession(player.getPlayer());
    }

    <T extends EditableMapData> SetupContext<T> setupModel(
            AnniPlayer player,
            MapModel<T> map
    ) throws UnsupportedOperationException;

    boolean terminateSession(MapModel<?> model);

    void addConfigurator(Class<? extends EditableMapData> dataType, EditorConfigurator configurator);

}
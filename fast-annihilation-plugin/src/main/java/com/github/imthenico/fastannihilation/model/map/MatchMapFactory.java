package com.github.imthenico.fastannihilation.model.map;

import com.github.imthenico.annihilation.api.model.map.MatchMap;
import com.github.imthenico.annihilation.api.model.map.MatchMapData;
import com.github.imthenico.gmlib.CustomMapFactory;
import com.github.imthenico.gmlib.metadata.MetadataSnapshot;
import com.github.imthenico.gmlib.world.AWorld;
import com.github.imthenico.gmlib.world.WorldContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MatchMapFactory implements CustomMapFactory<MatchMapData, MatchMap> {
    @Override
    public @Nullable MatchMap createMap(
            @NotNull AWorld aWorld,
            @NotNull WorldContainer<AWorld> worldContainer,
            @NotNull String s,
            @NotNull MatchMapData matchMapData,
            @NotNull MetadataSnapshot metadataSnapshot
    ) {
        WorldContainer<AWorld> allWorlds = new WorldContainer<>(worldContainer);
        allWorlds.add(aWorld);

        return new MatchMap(allWorlds, matchMapData);
    }
}
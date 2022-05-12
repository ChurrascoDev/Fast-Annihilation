package com.github.imthenico.annihilation.api.model;

import com.github.imthenico.gmlib.MapModel;
import com.github.imthenico.gmlib.ModelData;

public interface ModelCache extends Iterable<MapModel<?>> {

    <T extends ModelData> MapModel<T> getModel(String name);

    boolean addModel(MapModel<?> mapModel);

    <T extends ModelData> MapModel<?> replaceModel(MapModel<T> mapModel);

    void removeModel(String name);

    boolean has(String name);

    void clear();

    int count();

}
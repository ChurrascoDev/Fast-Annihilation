package com.github.imthenico.annihilation.api.model.map.data;

import com.github.imthenico.gmlib.ModelData;

public abstract class EditableMapData extends ModelData {

    public abstract boolean editable();

    public abstract void accept(EditableMapData mapData);

    public abstract EditableMapData copy();

}
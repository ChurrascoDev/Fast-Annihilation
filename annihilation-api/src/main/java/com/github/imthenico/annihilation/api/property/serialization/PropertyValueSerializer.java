package com.github.imthenico.annihilation.api.property.serialization;

import com.github.imthenico.simplecommons.data.node.TreeNode;

public interface PropertyValueSerializer<T> {

    TreeNode serializeProperty(T property);

}
package com.github.imthenico.annihilation.api.property.serialization;

import com.github.imthenico.annihilation.api.property.PropertiesContainer;
import com.github.imthenico.annihilation.api.property.PropertyKey;
import com.github.imthenico.simplecommons.data.node.AdapterNode;
import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.util.list.CustomList;
import com.github.imthenico.simplecommons.value.AbstractValue;

import java.util.Collection;

@FunctionalInterface
public interface DataInterceptor {

    void intercept(PropertiesContainer propertiesContainer, AdapterNode treeNode);

    default void set(PropertyKey key, PropertiesContainer properties, TreeNode node) {
        NodeValue nodeValue = node.get(key.getName());
        Object value = nodeValue.getValue();

        if (value instanceof Collection<?>) {
            CustomList<Object> customList = CustomList.create();

            customList.addAll((Collection<?>) value);

            properties.set(key, customList);
        } else if (value instanceof AbstractValue) {
            properties.set(key, ((AbstractValue) value).getValue());
        }
    }
}
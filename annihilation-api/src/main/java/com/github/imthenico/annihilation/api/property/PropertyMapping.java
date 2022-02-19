package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.property.serialization.DataInterceptor;import com.github.imthenico.annihilation.api.property.serialization.PropertyValueSerializer;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.node.TreeNode;

public interface PropertyMapping {

    void addDataInterceptor(DataInterceptor... dataInterceptors);

    void bindSerializer(PropertyValueSerializer<Object> serializer, Class<?>... classes);

    <T> PropertyValueSerializer<T> getSerializer(Class<T> clazz);

    TreeNode serializeProperties(PropertiesContainer properties);

    PropertiesContainer deserializeProperties(TreeNode node);

    void setSerializationMapper(GenericMapper<String> mapper);

    GenericMapper<String> getSerializationMapper();

}
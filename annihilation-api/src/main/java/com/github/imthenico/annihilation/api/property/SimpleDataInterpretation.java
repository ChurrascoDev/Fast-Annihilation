package com.github.imthenico.annihilation.api.property;

import com.github.imthenico.annihilation.api.util.MapPropertiesHelper;
import com.github.imthenico.annihilation.api.property.serialization.*;
import com.github.imthenico.simplecommons.data.mapper.GenericMapper;
import com.github.imthenico.simplecommons.data.mapper.gson.GsonMapper;
import com.github.imthenico.simplecommons.data.node.AdapterNode;
import com.github.imthenico.simplecommons.data.node.NodeValue;
import com.github.imthenico.simplecommons.data.node.NodeValueList;
import com.github.imthenico.simplecommons.data.node.TreeNode;
import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValue;
import com.github.imthenico.simplecommons.data.node.value.SimpleNodeValueList;
import com.github.imthenico.simplecommons.util.Validate;

import java.util.*;

public class SimpleDataInterpretation implements PropertyMapping {

    private final List<DataInterceptor> interceptors;
    private final Map<Class<?>, PropertyValueSerializer<?>> serializers;
    private GenericMapper<String> globalMapper;

    public SimpleDataInterpretation() {
        this.interceptors = new ArrayList<>();
        this.serializers = new HashMap<>();
        this.globalMapper = new GsonMapper();

        addDataInterceptor(new DefaultDataInterceptor());
    }

    @Override
    public void addDataInterceptor(DataInterceptor... dataInterceptors) {
        interceptors.addAll(Arrays.asList(dataInterceptors));
    }

    @Override
    public void bindSerializer(PropertyValueSerializer<Object> serializer, Class<?>... classes) {
        for (Class<?> aClass : classes) {
            serializers.put(aClass, Validate.notNull(serializer));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> PropertyValueSerializer<T> getSerializer(Class<T> clazz) {
        return (PropertyValueSerializer<T>) serializers.get(clazz);
    }

    @Override
    @SuppressWarnings("unchecked")
    public TreeNode serializeProperties(PropertiesContainer properties) {
        TreeNode node = TreeNode.create();

        for (PropertyKey key : properties.keys()) {
            Object property = properties.getProperty(key);

            Object serialized;

            if (property instanceof Collection) {
                Collection<Object> propertyList = (Collection<Object>) property;

                NodeValueList values = new SimpleNodeValueList(new LinkedList<>());

                for (Object mapProperty : propertyList) {
                    values.add(trySerialize(mapProperty));
                }

                serialized = values;
            } else {
                serialized = trySerialize(property);
            }

            node.set(key.name(), serialized);
        }

        return node;
    }

    @Override
    public PropertiesContainer deserializeProperties(TreeNode node) {
        PropertiesContainer properties = new SimplePropertiesContainer();

        AdapterNode adapterNode = new AdapterNode(node, globalMapper);

        for (DataInterceptor interceptor : interceptors) {
            interceptor.intercept(properties, adapterNode);
        }

        node.forEach((k, v) -> {
            if (v.getAsNode().isPresent())
                return;

            PropertyKey key = PropertyKey.of(k);
            if (!properties.has(key))  {
                properties.set(key, v.getValue());
            }
        }, true);

        return properties;
    }

    @Override
    public void setSerializationMapper(GenericMapper<String> mapper) {
        this.globalMapper = Validate.notNull(mapper);
    }

    @Override
    public GenericMapper<String> getSerializationMapper() {
        return globalMapper;
    }

    @SuppressWarnings("unchecked")
    private NodeValue trySerialize(Object property) {
        if (property == null)
            return SimpleNodeValue.EMPTY;

        if (property instanceof NodeValue) {
            return  (NodeValue) property;
        }

        Object value = null;

        if (property instanceof SerializableValue) {
            Map<String, Object> serialized = ((SerializableValue) property).serialize();
            TreeNode treeNode = TreeNode.create();

            treeNode.set(serialized);

            value = treeNode;
        }

        PropertyValueSerializer<Object> propertyValueSerializer = getSerializer((Class<Object>) property.getClass());

        if (propertyValueSerializer != null) {
            value = propertyValueSerializer.serializeProperty(property);
        }

        if (value == null) {
            TreeNode serialized = globalMapper.mapDirectly(property, TreeNode.class);

            Validate.isTrue(serialized != null, "Unable to serialize property");

            value = serialized;
        }

        return new SimpleNodeValue(value);
    }
}
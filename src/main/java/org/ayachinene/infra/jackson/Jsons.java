package org.ayachinene.infra.jackson;

import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Map;

@Component
public class Jsons {

    private final ObjectMapper objectMapper;

    public Jsons(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (tools.jackson.core.JacksonException exception) {
            throw new JacksonException(exception);
        }
    }

    public byte[] writeValueAsBytes(Object value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (tools.jackson.core.JacksonException exception) {
            throw new JacksonException(exception);
        }
    }

    public <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (tools.jackson.core.JacksonException exception) {
            throw new JacksonException(exception);
        }
    }

    public JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (tools.jackson.core.JacksonException exception) {
            throw new JacksonException(exception);
        }
    }

    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public <T> T convertValue(
        Object fromValue,
        TypeReference<T> toValueType
    ) {
        return objectMapper.convertValue(fromValue, toValueType);
    }

    public <T> Map<String, Object> toMap(T value) {
        return objectMapper.convertValue(value, new TypeReference<>() {
        });
    }

    public <T> JsonNode toJson(T value) {
        return objectMapper.valueToTree(value);
    }
}

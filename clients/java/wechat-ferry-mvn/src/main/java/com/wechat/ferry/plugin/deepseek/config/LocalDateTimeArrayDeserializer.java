package com.wechat.ferry.plugin.deepseek.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeArrayDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray() && node.size() == 7) {
            int year = node.get(0).asInt();
            int month = node.get(1).asInt();
            int day = node.get(2).asInt();
            int hour = node.get(3).asInt();
            int minute = node.get(4).asInt();
            int second = node.get(5).asInt();
            int nano = node.get(6).asInt();
            return LocalDateTime.of(year, month, day, hour, minute, second, nano);
        }
        throw new IllegalArgumentException("Invalid LocalDateTime array format");
    }
}

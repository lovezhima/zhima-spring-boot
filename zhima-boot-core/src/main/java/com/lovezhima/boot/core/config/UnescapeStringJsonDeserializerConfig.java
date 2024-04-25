package com.lovezhima.boot.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Json反序列化转义符
 *
 * @author king
 */
public class UnescapeStringJsonDeserializerConfig extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return jsonParser.getValueAsString().replace("\\", "");
    }
}
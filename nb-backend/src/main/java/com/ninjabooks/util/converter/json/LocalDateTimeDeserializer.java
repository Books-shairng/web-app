package com.ninjabooks.util.converter.json;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * @author Piotr 'pitrecki' Nowak
 * @since 1.0
 */
public final class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime>
{
    private static final long serialVersionUID = 4650561581695812005L;

    protected LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return LocalDateTime.parse(p.readValueAs(String.class));
    }
}

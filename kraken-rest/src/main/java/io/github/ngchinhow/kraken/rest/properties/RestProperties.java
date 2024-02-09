package io.github.ngchinhow.kraken.rest.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.ngchinhow.kraken.rest.decoder.KrakenResponseTypeFactory;

public final class RestProperties {

    private RestProperties() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static final String REST_API_HOST = "https://api.kraken.com";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setTypeFactory(new KrakenResponseTypeFactory());
    }
}

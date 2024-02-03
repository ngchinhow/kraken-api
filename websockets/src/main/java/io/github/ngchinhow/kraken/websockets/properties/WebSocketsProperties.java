package io.github.ngchinhow.kraken.websockets.properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

public final class WebSocketsProperties {

    private WebSocketsProperties() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class");
    }

    public static final URI PUBLIC_API_URL;
    public static final URI PRIVATE_API_URL;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final BigInteger REQ_ID_MAX_LIMIT = new BigInteger("18446744073709551616");

    static {
        try {
            PUBLIC_API_URL = new URI("wss://ws.kraken.com/v2");
            PRIVATE_API_URL = new URI("wss://ws-auth.kraken.com/v2");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

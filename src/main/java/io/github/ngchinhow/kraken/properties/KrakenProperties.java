package io.github.ngchinhow.kraken.properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class KrakenProperties {
    public static final String KRAKEN_REST_API_HOST = "https://api.kraken.com";
    public static final URI KRAKEN_WEBSOCKET_API_PUBLIC_URL;
    public static final URI KRAKEN_WEBSOCKET_API_PRIVATE_URL;
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        try {
            KRAKEN_WEBSOCKET_API_PUBLIC_URL = new URI("wss://ws.kraken.com/v2");
            KRAKEN_WEBSOCKET_API_PRIVATE_URL = new URI("wss://ws-auth.kraken.com/v2");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static final BigInteger KRAKEN_REQ_ID_MAX_LIMIT = new BigInteger("18446744073709551616");
}

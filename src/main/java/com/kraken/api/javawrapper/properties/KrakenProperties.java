package com.kraken.api.javawrapper.properties;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

public class KrakenProperties {
    public static final String KRAKEN_REST_API_BASE_URI = "https://api.kraken.com/0";
    public static final URI KRAKEN_WEBSOCKET_API_PUBLIC_URL;
    public static final URI KRAKEN_WEBSOCKET_API_PRIVATE_URL;

    static {
        try {
            KRAKEN_WEBSOCKET_API_PUBLIC_URL = new URI("wss://ws.kraken.com");
            KRAKEN_WEBSOCKET_API_PRIVATE_URL = new URI("wss://ws-auth.kraken.com");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static final BigInteger KRAKEN_REQ_ID_MAX_LIMIT = new BigInteger("18446744073709551616");
}

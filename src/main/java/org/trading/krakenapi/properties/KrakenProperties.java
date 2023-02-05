package org.trading.krakenapi.properties;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "kraken.api")
public class KrakenProperties {

    private final Rest rest = new Rest();
    private final Websocket websocket = new Websocket();

    @Data
    public static class Rest {
        private String baseUri;
    }

    @Data
    public static class Websocket {
        private String publicUrl;
        private String privateUrl;
    }
}

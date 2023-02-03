package org.trading.krakenapi.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.trading.krakenapi.threading.DaemonThreadFactory;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;
import org.trading.krakenapi.websocket.handler.ClientWebSocketHandler;

@Configuration
public class WebSocketConfiguration {
    private static final String KRAKEN_PUBLIC_WS_URL = "wss://ws.kraken.com";
    private static final String KRAKEN_PRIVATE_WS_URL = "wss://ws-auth.kraken.com";
    private final ObjectProvider<ClientWebSocketHandler> objectProvider;

    public WebSocketConfiguration(ObjectProvider<ClientWebSocketHandler> objectProvider) {
        this.objectProvider = objectProvider;
    }

    @Bean
    public WebSocketConnectionManager webSocketConnectionManager(DaemonThreadFactory daemonThreadFactory) {
        WebSocketConnectionManager manager = new WebSocketConnectionManager(
            new StandardWebSocketClient(),
            objectProvider.getObject(WebSocketEnumerations.CHANNEL.TICKER, "XBT/USD"),
            KRAKEN_PUBLIC_WS_URL
        );
        manager.setAutoStartup(true);
        return manager;
    }
}

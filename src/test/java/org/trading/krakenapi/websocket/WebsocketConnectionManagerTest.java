package org.trading.krakenapi.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.trading.krakenapi.manager.KrakenConnectionManager;
import org.trading.krakenapi.websocket.dto.general.SubscriptionEmbeddedObject;
import org.trading.krakenapi.websocket.enums.WebSocketEnumerations;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketConnectionManagerTest {
    private final AutowireCapableBeanFactory beanFactory;

    @Autowired
    public WebsocketConnectionManagerTest(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Test
    public void givenCredentials_whenGettingConnectionManager_thenSuccess() {
        KrakenConnectionManager krakenConnectionManager = new KrakenConnectionManager("", "");
        beanFactory.autowireBeanProperties(krakenConnectionManager, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
        SubscriptionEmbeddedObject subscription = SubscriptionEmbeddedObject.builder()
            .name(WebSocketEnumerations.CHANNEL.TICKER)
            .build();
        WebSocketConnectionManager webSocketConnectionManager = krakenConnectionManager
            .getWebsocketConnectionManager("XBT/USD", subscription);
        webSocketConnectionManager.start();
    }
}

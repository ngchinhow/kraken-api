package io.github.ngchinhow.kraken.rest;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import io.github.ngchinhow.kraken.rest.client.WebSocketsAuthenticationClient;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WebsocketsAuthenticationClientTest {

    private static final WebSocketsAuthenticationClient WEB_SOCKETS_AUTHENTICATION_CLIENT = new KrakenConnectionManager(
        System.getenv("KRAKEN_REST_API_KEY"),
        System.getenv("KRAKEN_REST_PRIVATE_KEY")
    ).getRestClient(WebSocketsAuthenticationClient.class);

    @Test
    public void givenAPICredentials_whenGetWebSocketsToken_thenSucceed() {
        WebSocketsTokenResult response = WEB_SOCKETS_AUTHENTICATION_CLIENT.getWebsocketsToken();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
    }
}

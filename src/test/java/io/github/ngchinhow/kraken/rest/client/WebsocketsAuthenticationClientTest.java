package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WebsocketsAuthenticationClientTest extends PrivateClientTest {

    private static WebSocketsAuthenticationClient CLIENT;

    @BeforeAll
    public static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(WebSocketsAuthenticationClient.class);
    }

    @Test
    public void givenAPICredentials_whenGetWebSocketsToken_thenSucceed() {
        WebSocketsTokenResult response = CLIENT.getWebsocketsToken();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getToken());
    }
}

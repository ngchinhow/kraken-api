package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebsocketsAuthenticationClientTest extends PrivateClientTest {

    private static WebSocketsAuthenticationClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = KRAKEN_CONNECTION_MANAGER.getRestClient(WebSocketsAuthenticationClient.class);
    }

    @Test
    void givenAPICredentials_whenGetWebSocketsToken_thenSucceed() {
        WebSocketsTokenResult response = CLIENT.getWebsocketsToken();
        assertThat(response)
            .isNotNull()
            .extracting(WebSocketsTokenResult::getToken)
            .isNotNull();
    }
}

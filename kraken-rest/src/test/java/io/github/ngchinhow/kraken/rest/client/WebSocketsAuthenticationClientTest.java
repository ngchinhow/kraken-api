package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.rest.factory.RestClientFactory;
import io.github.ngchinhow.kraken.rest.model.websocketsauthentication.token.WebSocketsTokenResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketsAuthenticationClientTest {

    private static WebSocketsAuthenticationClient CLIENT;

    @BeforeAll
    static void beforeAll() {
        CLIENT = RestClientFactory.getPrivateRestClient(
            WebSocketsAuthenticationClient.class,
            System.getenv("KRAKEN_REST_API_KEY"),
            System.getenv("KRAKEN_REST_PRIVATE_KEY"));
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

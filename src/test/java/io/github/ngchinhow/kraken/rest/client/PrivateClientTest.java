package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;

public abstract class PrivateClientTest {
    protected static final KrakenConnectionManager KRAKEN_CONNECTION_MANAGER =
        new KrakenConnectionManager(
            System.getenv("KRAKEN_REST_API_KEY"),
            System.getenv("KRAKEN_REST_PRIVATE_KEY")
        );
}

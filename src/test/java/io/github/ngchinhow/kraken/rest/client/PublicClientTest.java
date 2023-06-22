package io.github.ngchinhow.kraken.rest.client;

import io.github.ngchinhow.kraken.manager.KrakenConnectionManager;
import org.apache.commons.lang3.StringUtils;

public abstract class PublicClientTest {
    protected static final KrakenConnectionManager KRAKEN_CONNECTION_MANAGER
        = new KrakenConnectionManager(StringUtils.EMPTY, StringUtils.EMPTY);
}

# Kraken Crypto Exchange API

This project wraps around both Kraken's [REST API (v1.1.0)](https://docs.kraken.com/rest/) and 
[WebSockets API (v2.0.0)](https://docs.kraken.com/websockets-v2/). Not all endpoints are implemented (e.g. REST User
Funding), so please feel free to fork the project and implement them yourself.

## Usage Guide
The entry point for the library is the `KrakenConnectionManager` class. Specify the API Key and Private Key generated 
from the Kraken UI as inputs to the constructor of the class.

```java
KrakenConnectionManager krakenConnectionManager = new KrakenConnectionManager("API_KEY", "PRIVATE_KEY");
```

Then, either the REST side or WebSockets side of the API can be retrieved from the connection manager.

```java
<T extends RestClient> T restClient = krakenConnectionManager.getRestClient(Class<T> tClass);
KrakenPublicWebSocketsClient publicWebSocketsClient = krakenConnectionManager.getKrakenPublicWebSocketClient();
KrakenPrivateWebSocketsClient publicWebSocketsClient = krakenConnectionManager.getKrakenPrivateWebSocketClient();
```

### REST
The list of RestClient subclasses map directly to Kraken's REST API documentation sections.

```
MarketDataClient
UserDataClient
UserFundingClient
UserStakingClient
UserSubaccountClient
UserTradingClient
WebSocketsAuthenticationClient
```

As shown in the documentation [Requests, Responses and Errors](https://docs.kraken.com/rest/#section/General-Usage/Requests-Responses-and-Errors),
every response has both an `error` and `result` field. This library unwraps this object and throws a `400` bad request 
exception if the `error` array is non-empty, and only passes through the `result` field to the user as `200`.

### WebSockets
To interact with channel subscriptions, use the `subscribe` and `unsubscribe` methods on the clients. Using the API Key
and Private Key supplied to the connection manager, the private WebSockets client automatically retrieves the WebSockets
token from the WebSockets Authentication REST endpoint (/private/GetWebSocketsToken) for authentication against private 
channels.

All channels on Kraken's WebSockets documentation are available for subscription.

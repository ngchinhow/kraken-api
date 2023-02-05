package org.trading.krakenapi.rest.client;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface UserTradingClient extends RestClient {

    @PostMapping(value = "/0/private/AddOrder", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void addOrder(@RequestBody Map<String, ?> form);
}

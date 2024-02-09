package io.github.ngchinhow.kraken.rest.factory;

import feign.Feign;
import feign.Logger;
import feign.form.FormEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import io.github.ngchinhow.kraken.rest.client.RestClient;
import io.github.ngchinhow.kraken.rest.encoder.ListJoinQueryMapEncoder;
import io.github.ngchinhow.kraken.rest.interceptor.KrakenRequestInterceptor;
import io.github.ngchinhow.kraken.rest.interceptor.KrakenResponseInterceptor;
import io.github.ngchinhow.kraken.rest.properties.RestProperties;

public class RestClientFactory {

    public static <T extends RestClient> T getRestClient(Class<T> tClass, String apiKey, String privateKey) {
        return Feign.builder()
                    .client(new OkHttpClient())
                    .decoder(new JacksonDecoder(RestProperties.OBJECT_MAPPER))
                    .encoder(new FormEncoder(new JacksonEncoder(RestProperties.OBJECT_MAPPER)))
                    .logger(new Slf4jLogger())
                    .logLevel(Logger.Level.FULL)
                    .requestInterceptor(new KrakenRequestInterceptor(apiKey, privateKey))
                    .responseInterceptor(new KrakenResponseInterceptor())
                    .queryMapEncoder(new ListJoinQueryMapEncoder())
                    .target(tClass, RestProperties.REST_API_HOST);
    }
}

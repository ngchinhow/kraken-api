package io.github.ngchinhow.kraken.rest.interceptor;

import feign.FeignException;
import feign.InvocationContext;
import feign.Request;
import feign.ResponseInterceptor;
import io.github.ngchinhow.kraken.rest.model.ResultInterface;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;

public class KrakenResponseInterceptor implements ResponseInterceptor {
    @Override
    public Object aroundDecode(InvocationContext invocationContext) {
        KrakenResponse<? extends ResultInterface> krakenResponse = (KrakenResponse<? extends ResultInterface>) invocationContext.proceed();
        if (!krakenResponse.getError().isEmpty()) {
            Request request = invocationContext.response().request();
            String message = String.join(", ", krakenResponse.getError());
            throw new FeignException.BadRequest(message, request, request.body(), request.headers());
        }
        return krakenResponse.getResult();
    }
}
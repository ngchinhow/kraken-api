package io.github.ngchinhow.kraken.rest.decoder;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.LookupCache;
import io.github.ngchinhow.kraken.rest.model.KrakenResponse;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;

public class KrakenResponseTypeFactory extends TypeFactory {

    public KrakenResponseTypeFactory() {
        super((LookupCache<Object, JavaType>) null);
    }

    @Override
    public JavaType constructType(Type type) {
        return _fromAny(null, TypeUtils.parameterize(KrakenResponse.class, type), EMPTY_BINDINGS);
    }
}

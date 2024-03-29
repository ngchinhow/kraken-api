package io.github.ngchinhow.kraken.rest.encoder;

import feign.querymap.FieldQueryMapEncoder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KrakenQueryMapEncoder extends FieldQueryMapEncoder {
    private static final String DEFAULT_SEPARATOR = ",";
    @Override
    public Map<String, Object> encode(Object object) {
        Map<String, Object> mapping = super.encode(object);
        for (Map.Entry<String, Object> entry : mapping.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof List<?> list) {
                mapping.replace(
                    entry.getKey(),
                    list.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(DEFAULT_SEPARATOR))
                );
            } else if (value instanceof ZonedDateTime zonedDateTime)
                mapping.replace(entry.getKey(), zonedDateTime.toEpochSecond());
        }
        return mapping;
    }
}

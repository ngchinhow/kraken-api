package io.github.ngchinhow.kraken.rest.model.userdata.openorders;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter(value = AccessLevel.PACKAGE)
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CloseOrder extends AbstractOrder {
    @JsonProperty("closetm")
    private ZonedDateTime closeTimestamp;
    private String reason;
}

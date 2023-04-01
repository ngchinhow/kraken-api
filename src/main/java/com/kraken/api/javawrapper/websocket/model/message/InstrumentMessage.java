package com.kraken.api.javawrapper.websocket.model.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kraken.api.javawrapper.websocket.enums.ChannelMetadata;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Jacksonized
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentMessage extends AbstractPublicationMessage {
    private InstrumentData data;

    {
        this.setChannel(ChannelMetadata.ChannelType.INSTRUMENT);
    }

    @Data
    @NoArgsConstructor(force = true)
    public static class InstrumentData {
        @NonNull
        private List<Asset> assets;
        @NonNull
        private List<Pair> pairs;

        @Data
        @NoArgsConstructor(force = true)
        public static class Asset {
            @NonNull
            private Boolean borrowable;
            @JsonProperty(value = "collateral_value", required = true)
            private BigDecimal collateralValue;
            @NonNull
            private String id;
            @JsonProperty("margin_rate")
            private BigDecimal marginRate;
            @NonNull
            private Integer precision;
            @JsonProperty(value = "precision_display", required = true)
            private Integer precisionDisplay;
            @NonNull
            private Status status;

            @SuppressWarnings("unused")
            private enum Status {
                DEPOSIT_ONLY("depositonly"),
                DISABLED("disabled"),
                ENABLED("enabled"),
                FUNDING_TEMPORARILY_DISABLED("fundingtemporarilydisabled"),
                WITHDRAWAL_ONLY("withdrawalonly"),
                WORK_IN_PROGRESS("workinprogress");

                private final String status;

                Status(String status) {
                    this.status = status;
                }

                @JsonValue
                public String getStatus() {
                    return status;
                }
            }
        }

        @Data
        @NoArgsConstructor(force = true)
        public static class Pair {
            @NonNull
            private String base;
            @JsonProperty(value = "cost_min", required = true)
            private BigDecimal minCost;
            @JsonProperty(value = "cost_precision", required = true)
            private int costPrecision;
            @JsonProperty(value = "has_index", required = true)
            private boolean hasIndex;
            @JsonProperty("margin_initial")
            private BigDecimal initialMargin;
            @NonNull
            private Boolean marginable;
            @JsonProperty("position_limit_long")
            private Integer positionLimitLong;
            @JsonProperty("position_limit_short")
            private Integer positionLimitShort;
            @JsonProperty(value = "price_increment", required = true)
            private BigDecimal priceIncrement;
            @JsonProperty(value = "price_precision", required = true)
            private Integer pricePrecision;
            @JsonProperty(value = "qty_increment", required = true)
            private BigDecimal quantityIncrement;
            @JsonProperty(value = "qty_min", required = true)
            private BigDecimal minQuantity;
            @JsonProperty(value = "qty_precision", required = true)
            private int quantityPrecision;
            @NonNull
            private String quote;
            @NonNull
            private Status status;
            @NonNull
            private String symbol;

            @SuppressWarnings("unused")
            private enum Status {
                CANCEL_ONLY("cancel_only"),
                DELISTED("delisted"),
                LIMIT_ONLY("limit_only"),
                MAINTENANCE("maintenance"),
                ONLINE("online"),
                POST_ONLY("post_only"),
                REDUCE_ONLY("reduce_only"),
                WORK_IN_PROGRESS("work_in_progress");

                private final String status;

                Status(String status) {
                    this.status = status;
                }

                @JsonValue
                public String getStatus() {
                    return status;
                }
            }
        }
    }
}

package com.tw.pos.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KafkaOutput {
    @JsonProperty("InvoiceNumber")
    private String invoiceNumber;

    @JsonProperty("Status")
    private String status;
}

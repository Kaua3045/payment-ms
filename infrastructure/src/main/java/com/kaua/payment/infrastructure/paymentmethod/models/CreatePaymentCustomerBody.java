package com.kaua.payment.infrastructure.paymentmethod.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePaymentCustomerBody(
        @JsonProperty("email") String email,
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("account_id") String accountId
) {
}

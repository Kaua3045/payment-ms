package com.kaua.payment.application.usecases.paymentcustomer.create;

public record CreatePaymentCustomerCommand(
        String email,
        String firstName,
        String lastName,
        String accountId,
        String xIdempotencyKey
) {

    public static CreatePaymentCustomerCommand with(
            final String email,
            final String firstName,
            final String lastName,
            final String accountId,
            final String xIdempotencyKey
    ) {
        return new CreatePaymentCustomerCommand(email, firstName, lastName, accountId, xIdempotencyKey);
    }
}

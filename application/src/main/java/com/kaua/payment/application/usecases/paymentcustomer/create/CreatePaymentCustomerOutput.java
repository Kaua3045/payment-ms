package com.kaua.payment.application.usecases.paymentcustomer.create;

public record CreatePaymentCustomerOutput(String paymentCustomerId, String email, String accountId) {

    public static CreatePaymentCustomerOutput from(String paymentCustomerId, String email, String accountId) {
        return new CreatePaymentCustomerOutput(paymentCustomerId, email, accountId);
    }
}

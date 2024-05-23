package com.kaua.payment.application.repositories;

public interface PaymentMethodRepository {

    CreatePaymentCustomerRepositoryResponse saveCustomerInfo(String customerId, String email, String accountId);

    record CreatePaymentCustomerRepositoryResponse(String paymentCustomerId) {}
}

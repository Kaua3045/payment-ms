package com.kaua.payment.application.gateways;

public interface PaymentMethodGateway {

    CreatePaymentCustomerGatewayResponse createCustomer(String email, String name, String lastName, String xIdempotencyKey);

    boolean existsCustomerByEmail(String customerEmail);

    record CreatePaymentCustomerGatewayResponse(String paymentCustomerId) {}
}

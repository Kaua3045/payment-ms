package com.kaua.payment.application.usecases.paymentcustomer.create;

import com.kaua.payment.application.gateways.PaymentMethodGateway;
import com.kaua.payment.application.repositories.PaymentMethodRepository;
import com.kaua.payment.domain.exception.DomainException;
import com.kaua.payment.domain.validation.Error;

import java.util.Objects;

public class DefaultCreatePaymentCustomerUseCase extends CreatePaymentCustomerUseCase {

    private final PaymentMethodGateway paymentMethodGateway;
    private final PaymentMethodRepository paymentMethodRepository;

    public DefaultCreatePaymentCustomerUseCase(
            final PaymentMethodGateway paymentMethodGateway,
            final PaymentMethodRepository paymentMethodRepository
    ) {
        this.paymentMethodGateway = Objects.requireNonNull(paymentMethodGateway);
        this.paymentMethodRepository = Objects.requireNonNull(paymentMethodRepository);
    }

    @Override
    public CreatePaymentCustomerOutput execute(CreatePaymentCustomerCommand input) {
        final var aExistsCustomer = this.paymentMethodGateway.existsCustomerByEmail(input.email());

        if (aExistsCustomer) throw DomainException.with(new Error("customer already exists"));

        final var aPaymentCustomerGatewayResponse = this.paymentMethodGateway.createCustomer(
                input.email(),
                input.firstName(),
                input.lastName(),
                input.xIdempotencyKey()
        );

        final var aPaymentCustomer = this.paymentMethodRepository.saveCustomerInfo(
                aPaymentCustomerGatewayResponse.paymentCustomerId(),
                input.email(),
                input.accountId()
        );

        // TODO: In future publish event to notify that a new customer was created and
        //  add more information to customer in mercado pago

        return CreatePaymentCustomerOutput.from(aPaymentCustomer.paymentCustomerId(), input.email(), input.accountId());
    }
}

package com.kaua.payment.infrastructure.configurations.usecases;

import com.kaua.payment.application.gateways.PaymentMethodGateway;
import com.kaua.payment.application.repositories.PaymentMethodRepository;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerUseCase;
import com.kaua.payment.application.usecases.paymentcustomer.create.DefaultCreatePaymentCustomerUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class PaymentMethodUseCaseConfig {

    private final PaymentMethodGateway paymentMethodGateway;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodUseCaseConfig(
            final PaymentMethodGateway paymentMethodGateway,
            final PaymentMethodRepository paymentMethodRepository
    ) {
        this.paymentMethodGateway = Objects.requireNonNull(paymentMethodGateway);
        this.paymentMethodRepository = Objects.requireNonNull(paymentMethodRepository);
    }

    @Bean
    public CreatePaymentCustomerUseCase createPaymentCustomerUseCase() {
        return new DefaultCreatePaymentCustomerUseCase(paymentMethodGateway, paymentMethodRepository);
    }
}

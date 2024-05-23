package com.kaua.payment.infrastructure.api.controllers;

import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerCommand;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerOutput;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerUseCase;
import com.kaua.payment.infrastructure.api.PaymentMethodAPI;
import com.kaua.payment.infrastructure.paymentmethod.models.CreatePaymentCustomerBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class PaymentMethodController implements PaymentMethodAPI {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodController.class);

    private final CreatePaymentCustomerUseCase createPaymentCustomerUseCase;

    public PaymentMethodController(
            final CreatePaymentCustomerUseCase createPaymentCustomerUseCase
    ) {
        this.createPaymentCustomerUseCase = Objects.requireNonNull(createPaymentCustomerUseCase);
    }

    @Override
    public ResponseEntity<CreatePaymentCustomerOutput> createPaymentCustomer(
            CreatePaymentCustomerBody body,
            String xIdempotencyKey
    ) {
        final var aCommand = CreatePaymentCustomerCommand.with(
                body.email(),
                body.firstName(),
                body.lastName(),
                body.accountId(),
                xIdempotencyKey
        );
        log.debug("Creating payment customer with email: {} and command: {}", body.email(), aCommand);

        final var aOutput = this.createPaymentCustomerUseCase.execute(aCommand);

        log.info("Created payment customer with email: {}, paymentCustomerId: {}, and accountId: {}", body.email(), aOutput.paymentCustomerId(), body.accountId());
        return ResponseEntity.status(HttpStatus.CREATED).body(aOutput);
    }
}

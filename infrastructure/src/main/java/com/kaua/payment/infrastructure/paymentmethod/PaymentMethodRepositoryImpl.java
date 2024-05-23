package com.kaua.payment.infrastructure.paymentmethod;

import com.kaua.payment.application.repositories.PaymentMethodRepository;
import com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaEntity;
import com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodRepositoryImpl.class);

    private final PaymentCustomerJpaRepository paymentCustomerJpaRepository;

    public PaymentMethodRepositoryImpl(final PaymentCustomerJpaRepository paymentCustomerJpaRepository) {
        this.paymentCustomerJpaRepository = Objects.requireNonNull(paymentCustomerJpaRepository);
    }

    @Override
    public PaymentMethodRepository.CreatePaymentCustomerRepositoryResponse saveCustomerInfo(String customerId, String email, String accountId) {
        final var aEntity = PaymentCustomerJpaEntity.create(email, accountId, customerId);

        log.debug("Saving PaymentCustomer: {}", aEntity);
        final var savedEntity = paymentCustomerJpaRepository.save(aEntity);

        log.info("PaymentCustomer saved: {}", savedEntity);
        return new CreatePaymentCustomerRepositoryResponse(savedEntity.getPaymentGatewayCustomerId());
    }
}

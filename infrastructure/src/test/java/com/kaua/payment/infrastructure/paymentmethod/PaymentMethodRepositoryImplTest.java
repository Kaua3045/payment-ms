package com.kaua.payment.infrastructure.paymentmethod;

import com.kaua.payment.domain.Fixture;
import com.kaua.payment.domain.utils.IdUtils;
import com.kaua.payment.infrastructure.DatabaseRepositoryTest;
import com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DatabaseRepositoryTest
public class PaymentMethodRepositoryImplTest {

    @Autowired
    private PaymentMethodRepositoryImpl paymentMethodRepositoryImpl;

    @Autowired
    private PaymentCustomerJpaRepository paymentCustomerJpaRepository;

    @Test
    void givenAValidValues_whenSaveCustomerInfo_thenShouldSaveCustomerInfo() {
        final var aPaymentCustomerId = IdUtils.generateId();
        final var aEmail = Fixture.randomEmail();
        final var aAccountId = IdUtils.generateId();

        Assertions.assertEquals(0, paymentCustomerJpaRepository.count());

        final var aPaymentCustomer = paymentMethodRepositoryImpl.saveCustomerInfo(
                aPaymentCustomerId,
                aEmail,
                aAccountId
        );

        Assertions.assertEquals(1, paymentCustomerJpaRepository.count());
        Assertions.assertEquals(aPaymentCustomerId, aPaymentCustomer.paymentCustomerId());
    }
}

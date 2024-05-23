package com.kaua.payment.infrastructure.paymentmethod.persistence;

import com.kaua.payment.domain.utils.IdUtils;
import com.kaua.payment.infrastructure.DatabaseRepositoryTest;
import org.hibernate.PropertyValueException;
import org.hibernate.id.IdentifierGenerationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

@DatabaseRepositoryTest
public class PaymentCustomerJpaRepositoryTest {

    @Autowired
    private PaymentCustomerJpaRepository paymentCustomerJpaRepository;

    @Test
    void givenAnInvalidNullPaymentGatewayCustomerId_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "paymentGatewayCustomerId";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaEntity.paymentGatewayCustomerId";

        final var aEntity = PaymentCustomerJpaEntity.create(
                "aEmail",
                "anAccountId",
                IdUtils.generateId()
        );
        aEntity.setPaymentGatewayCustomerId(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> paymentCustomerJpaRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullEmail_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "email";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaEntity.email";

        final var aEntity = PaymentCustomerJpaEntity.create(
                "aEmail",
                "anAccountId",
                IdUtils.generateId()
        );
        aEntity.setEmail(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> paymentCustomerJpaRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullAccountId_whenCallSave_shouldReturnAnException() {
        final var expectedPropertyName = "accountId";
        final var expectedErrorMessage = "not-null property references a null or transient value : com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaEntity.accountId";

        final var aEntity = PaymentCustomerJpaEntity.create(
                "aEmail",
                "anAccountId",
                IdUtils.generateId()
        );
        aEntity.setAccountId(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class,
                () -> paymentCustomerJpaRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullId_whenCallSave_shouldReturnAnException() {
        final var expectedErrorMessage = "Identifier of entity 'com.kaua.payment.infrastructure.paymentmethod.persistence.PaymentCustomerJpaEntity' must be manually assigned before calling 'persist()'";

        final var aEntity = PaymentCustomerJpaEntity.create(
                "aEmail",
                "anAccountId",
                IdUtils.generateId()
        );
        aEntity.setId(null);

        final var actualException = Assertions.assertThrows(JpaSystemException.class,
                () -> paymentCustomerJpaRepository.save(aEntity));

        final var actualCause = Assertions.assertInstanceOf(IdentifierGenerationException.class,
                actualException.getCause());

        Assertions.assertEquals(expectedErrorMessage, actualCause.getMessage());
    }

    @Test
    void givenAnValidEntity_whenCallSave_shouldReturnTheEntity() {
        final var aEntity = PaymentCustomerJpaEntity.create(
                "aEmail",
                "anAccountId",
                IdUtils.generateId()
        );

        final var actualEntity = paymentCustomerJpaRepository.save(aEntity);

        Assertions.assertNotNull(actualEntity);
        Assertions.assertEquals(aEntity.getId(), actualEntity.getId());
        Assertions.assertEquals(aEntity.getEmail(), actualEntity.getEmail());
        Assertions.assertEquals(aEntity.getAccountId(), actualEntity.getAccountId());
        Assertions.assertEquals(aEntity.getPaymentGatewayCustomerId(), actualEntity.getPaymentGatewayCustomerId());
    }
}

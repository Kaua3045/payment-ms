package com.kaua.payment.application.usecases.paymentcustomer.create;

import com.kaua.payment.application.UseCaseTest;
import com.kaua.payment.application.gateways.PaymentMethodGateway;
import com.kaua.payment.application.repositories.PaymentMethodRepository;
import com.kaua.payment.domain.Fixture;
import com.kaua.payment.domain.exception.DomainException;
import com.kaua.payment.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CreatePaymentCustomerUseCaseTest extends UseCaseTest {

    @Mock
    private PaymentMethodGateway paymentMethodGateway;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @InjectMocks
    private DefaultCreatePaymentCustomerUseCase createPaymentCustomerUseCase;

    @Test
    void givenAValidCommand_whenCallCreatePaymentCustomer_thenShouldCreatePaymentCustomer() {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();
        final var aXIdempotencyKey = IdUtils.generateId();
        final var aPaymentCustomerId = IdUtils.generateId();

        final var aCommand = CreatePaymentCustomerCommand.with(
                aEmail,
                aFirstName,
                aLastName,
                aAccountId,
                aXIdempotencyKey
        );

        Mockito.when(paymentMethodGateway.existsCustomerByEmail(aEmail)).thenReturn(false);
        Mockito.when(paymentMethodGateway.createCustomer(aEmail, aFirstName, aLastName, aXIdempotencyKey))
                .thenAnswer(t -> new PaymentMethodGateway.CreatePaymentCustomerGatewayResponse(aPaymentCustomerId));
        Mockito.when(paymentMethodRepository.saveCustomerInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenAnswer(t -> new PaymentMethodRepository.CreatePaymentCustomerRepositoryResponse(aPaymentCustomerId));

        final var aOutput = this.createPaymentCustomerUseCase.execute(aCommand);

        Assertions.assertNotNull(aOutput);
        Assertions.assertEquals(aPaymentCustomerId, aOutput.paymentCustomerId());
        Assertions.assertEquals(aEmail, aOutput.email());
        Assertions.assertEquals(aAccountId, aOutput.accountId());

        Mockito.verify(paymentMethodGateway, Mockito.times(1)).existsCustomerByEmail(aEmail);
        Mockito.verify(paymentMethodGateway, Mockito.times(1)).createCustomer(aEmail, aFirstName, aLastName, aXIdempotencyKey);
    }

    @Test
    void givenAnExistsEmail_whenCallCreatePaymentCustomer_thenShouldThrowDomainException() {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();
        final var aXIdempotencyKey = IdUtils.generateId();

        final var aCommand = CreatePaymentCustomerCommand.with(
                aEmail,
                aFirstName,
                aLastName,
                aAccountId,
                aXIdempotencyKey
        );

        Mockito.when(paymentMethodGateway.existsCustomerByEmail(aEmail)).thenReturn(true);

        final var aException = Assertions.assertThrows(DomainException.class,
                () -> this.createPaymentCustomerUseCase.execute(aCommand));

        Assertions.assertEquals("customer already exists", aException.getErrors().get(0).message());

        Mockito.verify(paymentMethodGateway, Mockito.times(1)).existsCustomerByEmail(aEmail);
        Mockito.verify(paymentMethodGateway, Mockito.never()).createCustomer(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    }
}

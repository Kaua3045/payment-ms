package com.kaua.payment.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerCommand;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerOutput;
import com.kaua.payment.application.usecases.paymentcustomer.create.CreatePaymentCustomerUseCase;
import com.kaua.payment.domain.Fixture;
import com.kaua.payment.domain.exception.DomainException;
import com.kaua.payment.domain.utils.IdUtils;
import com.kaua.payment.domain.validation.Error;
import com.kaua.payment.infrastructure.ControllerTest;
import com.kaua.payment.infrastructure.exceptions.PaymentGatewayException;
import com.kaua.payment.infrastructure.paymentmethod.models.CreatePaymentCustomerBody;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.net.MPResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;

@ControllerTest(controllers = PaymentMethodAPI.class)
public class PaymentMethodAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreatePaymentCustomerUseCase createPaymentCustomerUseCase;

    @Test
    void givenAValidInput_whenCreatePaymentCustomer_thenReturns201() throws Exception {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();
        final var aPaymentCustomerId = IdUtils.generateId();

        final var aBody = new CreatePaymentCustomerBody(aEmail, aFirstName, aLastName, aAccountId);

        Mockito.when(createPaymentCustomerUseCase.execute(Mockito.any()))
                .thenReturn(CreatePaymentCustomerOutput.from(aPaymentCustomerId, aEmail, aAccountId));

        final var aRequest = MockMvcRequestBuilders.post("/v1/payment-methods/customers")
                .contentType("application/json")
                .header("x-idempotency-key", IdUtils.generateId())
                .content(mapper.writeValueAsString(aBody));

        this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payment_customer_id").value(aPaymentCustomerId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(aEmail))
                .andExpect(MockMvcResultMatchers.jsonPath("$.account_id").value(aAccountId));

        final var aCmdCaptor = ArgumentCaptor.forClass(CreatePaymentCustomerCommand.class);
        Mockito.verify(createPaymentCustomerUseCase, Mockito.times(1)).execute(aCmdCaptor.capture());

        final var aCmd = aCmdCaptor.getValue();
        Assertions.assertEquals(aEmail, aCmd.email());
        Assertions.assertEquals(aFirstName, aCmd.firstName());
        Assertions.assertEquals(aLastName, aCmd.lastName());
        Assertions.assertEquals(aAccountId, aCmd.accountId());
    }

    @Test
    void givenAExistsCustomerEmail_whenCreatePaymentCustomer_thenThrowsDomainException() throws Exception {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();

        final var aBody = new CreatePaymentCustomerBody(aEmail, aFirstName, aLastName, aAccountId);

        Mockito.when(createPaymentCustomerUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error("customer already exists")));

        final var aRequest = MockMvcRequestBuilders.post("/v1/payment-methods/customers")
                .contentType("application/json")
                .header("x-idempotency-key", IdUtils.generateId())
                .content(mapper.writeValueAsString(aBody));

        this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message").value("customer already exists"));
    }

    @Test
    void givenAValidInputButThrowsUnexpectedException_whenCreatePaymentCustomer_thenReturns500() throws Exception {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();

        final var aBody = new CreatePaymentCustomerBody(aEmail, aFirstName, aLastName, aAccountId);

        Mockito.when(createPaymentCustomerUseCase.execute(Mockito.any()))
                .thenThrow(new RuntimeException("Unexpected exception"));

        final var aRequest = MockMvcRequestBuilders.post("/v1/payment-methods/customers")
                .contentType("application/json")
                .header("x-idempotency-key", IdUtils.generateId())
                .content(mapper.writeValueAsString(aBody));

        this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void givenAValidInputButThrowsPaymentGatewayExceptionOnAPIExceptionCause_whenCreatePaymentCustomer_thenReturnBadRequest() throws Exception {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();

        final var aBody = new CreatePaymentCustomerBody(aEmail, aFirstName, aLastName, aAccountId);

        Mockito.when(createPaymentCustomerUseCase.execute(Mockito.any()))
                .thenThrow(new PaymentGatewayException("API error while searching customer in Mercado Pago",
                        new MPApiException("Api ERROR",
                                new MPResponse(400, Map.of(), "API ERROR"))));

        final var aRequest = MockMvcRequestBuilders.post("/v1/payment-methods/customers")
                .contentType("application/json")
                .header("x-idempotency-key", IdUtils.generateId())
                .content(mapper.writeValueAsString(aBody));

        this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("API ERROR"));
    }

    @Test
    void givenAValidInputButThrowsPaymentGatewayExceptionOnUnexpectedExceptionCause_whenCreatePaymentCustomer_thenReturnInternalServerError() throws Exception {
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aAccountId = IdUtils.generateId();

        final var aBody = new CreatePaymentCustomerBody(aEmail, aFirstName, aLastName, aAccountId);

        Mockito.when(createPaymentCustomerUseCase.execute(Mockito.any()))
                .thenThrow(new PaymentGatewayException("Unexpected exception on searching customer in Mercado Pago",
                        new RuntimeException("Unexpected exception")));

        final var aRequest = MockMvcRequestBuilders.post("/v1/payment-methods/customers")
                .contentType("application/json")
                .header("x-idempotency-key", IdUtils.generateId())
                .content(mapper.writeValueAsString(aBody));

        this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Internal server error"));
    }
}

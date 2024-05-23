package com.kaua.payment.infrastructure.paymentmethod;

import com.kaua.payment.application.gateways.PaymentMethodGateway;
import com.kaua.payment.infrastructure.configurations.properties.mercadopago.MercadoPagoProperties;
import com.kaua.payment.infrastructure.exceptions.PaymentGatewayException;
import com.mercadopago.client.customer.CustomerClient;
import com.mercadopago.client.customer.CustomerRequest;
import com.mercadopago.core.MPRequestOptions;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPSearchRequest;
import com.mercadopago.resources.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class PaymentMethodMercadoPagoGateway implements PaymentMethodGateway {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodMercadoPagoGateway.class);

    private final MercadoPagoProperties mercadoPagoProperties;
    private CustomerClient customerClient;

    public PaymentMethodMercadoPagoGateway(
            final MercadoPagoProperties mercadoPagoProperties
    ) {
        this.mercadoPagoProperties = Objects.requireNonNull(mercadoPagoProperties);
        this.customerClient = new CustomerClient();
    }

    @Override
    public PaymentMethodGateway.CreatePaymentCustomerGatewayResponse createCustomer(String email, String name, String lastName, String xIdempotencyKey) {
        CustomerRequest customerRequest = CustomerRequest.builder()
                .email(email)
                .firstName(name)
                .lastName(lastName)
                .build();

        log.debug("Creating customer request object: {}", customerRequest);

        try {
            log.debug("Creating customer in Mercado Pago with x-idempotency-key: {} and email: {}", xIdempotencyKey, email);
            final var aCustomer = customerClient.create(customerRequest, this.getMPRequestOptions(xIdempotencyKey));
            log.info("Customer created in Mercado Pago: {} with x-idempotency-key {}", aCustomer.getId(), xIdempotencyKey);
            return new CreatePaymentCustomerGatewayResponse(aCustomer.getId());
        } catch (MPException e) {
            throw new PaymentGatewayException("Unexpected exception on create customer in Mercado Pago", e);
        } catch (MPApiException e) {
            throw new PaymentGatewayException("API error while create customer in Mercado Pago", e);
        }
    }

    @Override
    public boolean existsCustomerByEmail(String customerEmail) {
        Map<String, Object> filters = new HashMap<>();
        filters.put("email", customerEmail);
        MPSearchRequest searchRequest = MPSearchRequest.builder()
                .offset(0)
                .limit(10)
                .filters(filters)
                .build();

        log.debug("Creating search request object: {}", searchRequest);

        try {
            log.debug("Searching customer in Mercado Pago with email: {}", customerEmail);
            List<Customer> aOutput = customerClient.search(searchRequest, this.getMPRequestOptions(null))
                    .getResults();
            log.info("Customers found {} with email: {}", aOutput.size(), customerEmail);
            return !aOutput.isEmpty();
        } catch (MPException e) {
            throw new PaymentGatewayException("Unexpected exception on searching customer in Mercado Pago", e);
        } catch (MPApiException e) {
            throw new PaymentGatewayException("API error while searching customer in Mercado Pago", e);
        }
    }

    private MPRequestOptions getMPRequestOptions(String xIdempotencyKey) {
        // TODO: Devemos pegar a idempotency key da mesma forma que faziamos anteriormente e quando
        // for necessário repassamos ela pra frente

        final var aIdempotencyKey = StringUtils.hasText(xIdempotencyKey)
                ? xIdempotencyKey
                : UUID.randomUUID().toString();

        Map<String, String> aHeaders = new HashMap<>();
        aHeaders.put("x-idempotency-key", aIdempotencyKey);

        return MPRequestOptions.builder()
                .accessToken(this.mercadoPagoProperties.getAccessToken())
                .connectionRequestTimeout(this.mercadoPagoProperties.getRequestTimeout())
                .connectionTimeout(this.mercadoPagoProperties.getConnectionTimeout())
                .customHeaders(aHeaders)
                .build();
    }

    public void setCustomerClient(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }
}

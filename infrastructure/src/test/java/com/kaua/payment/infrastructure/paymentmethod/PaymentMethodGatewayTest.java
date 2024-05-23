package com.kaua.payment.infrastructure.paymentmethod;

import com.kaua.payment.domain.Fixture;
import com.kaua.payment.domain.utils.IdUtils;
import com.kaua.payment.infrastructure.UnitTest;
import com.kaua.payment.infrastructure.configurations.properties.mercadopago.MercadoPagoProperties;
import com.kaua.payment.infrastructure.exceptions.PaymentGatewayException;
import com.mercadopago.client.customer.CustomerClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPJsonParseException;
import com.mercadopago.net.MPResponse;
import com.mercadopago.net.MPResultsResourcesPage;
import com.mercadopago.resources.ResultsPaging;
import com.mercadopago.resources.customer.Customer;
import com.mercadopago.serialization.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

@UnitTest
public class PaymentMethodGatewayTest {

    @Test
    void givenAValidValues_whenCallCreateCustomer_thenShouldReturnACustomerId() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aXIdempotencyKey = IdUtils.generateId();

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.create(Mockito.any(), Mockito.any()))
                .thenReturn(getCustomer());

        final var aOutput = paymentMethodGateway.createCustomer(aEmail, aFirstName, aLastName, aXIdempotencyKey);

        Assertions.assertNotNull(aOutput);
        Assertions.assertNotNull(aOutput.paymentCustomerId());
        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).create(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAnInvalidValues_whenCallCreateCustomer_thenThrowsUnexpectedException() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aXIdempotencyKey = IdUtils.generateId();

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.create(Mockito.any(), Mockito.any()))
                .thenThrow(new MPException("Unexpected exception on create customer in Mercado Pago"));

        Assertions.assertThrows(PaymentGatewayException.class,
                () -> paymentMethodGateway.createCustomer(aEmail, aFirstName, aLastName, aXIdempotencyKey));

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).create(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAnInvalidValues_whenCallCreateCustomer_thenThrowsApiErrorException() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();
        final var aFirstName = Fixture.randomFirstName();
        final var aLastName = Fixture.randomLastName();
        final var aXIdempotencyKey = IdUtils.generateId();

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.create(Mockito.any(), Mockito.any()))
                .thenThrow(new MPApiException("Unexpected exception on create customer in Mercado Pago", new MPResponse(422, Map.of(), "An error message")));

        Assertions.assertThrows(PaymentGatewayException.class,
                () -> paymentMethodGateway.createCustomer(aEmail, aFirstName, aLastName, aXIdempotencyKey));

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).create(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAValidValues_whenCallExistsCustomerByEmail_thenShouldReturnTrue() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();

        final var aResultsResource = new MPResultsResourcesPage<Customer>();
        aResultsResource.setResults(List.of(getCustomer()));

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.search(Mockito.any(), Mockito.any()))
                .thenReturn(aResultsResource);

        final var aOutput = paymentMethodGateway.existsCustomerByEmail(aEmail);

        Assertions.assertTrue(aOutput);

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).search(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAValidValues_whenCallExistsCustomerByEmail_thenShouldReturnFalse() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();

        final var aResultsResource = new MPResultsResourcesPage<Customer>();
        aResultsResource.setResults(List.of());

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.search(Mockito.any(), Mockito.any()))
                .thenReturn(aResultsResource);

        final var aOutput = paymentMethodGateway.existsCustomerByEmail(aEmail);

        Assertions.assertFalse(aOutput);

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).search(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAnInvalidValues_whenCallExistsCustomerByEmail_thenThrowsUnexpectedException() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.search(Mockito.any(), Mockito.any()))
                .thenThrow(new MPException("Unexpected exception on searching customer in Mercado Pago"));

        Assertions.assertThrows(PaymentGatewayException.class,
                () -> paymentMethodGateway.existsCustomerByEmail(aEmail));

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).search(Mockito.any(), Mockito.any());
    }

    @Test
    void givenAnInvalidValues_whenCallExistsCustomerByEmail_thenThrowsApiErrorException() throws MPException, MPApiException {
        final var aMockedCustomerClient = Mockito.mock(CustomerClient.class);
        final var aEmail = Fixture.randomEmail();

        final var paymentMethodGateway = new PaymentMethodMercadoPagoGateway(getMercadoPagoProperties());
        paymentMethodGateway.setCustomerClient(aMockedCustomerClient);

        Mockito.when(aMockedCustomerClient.search(Mockito.any(), Mockito.any()))
                .thenThrow(new MPApiException("API error while searching customer in Mercado Pago", new MPResponse(422, Map.of(), "An error message")));

        Assertions.assertThrows(PaymentGatewayException.class,
                () -> paymentMethodGateway.existsCustomerByEmail(aEmail));

        Mockito.verify(aMockedCustomerClient, Mockito.times(1)).search(Mockito.any(), Mockito.any());
    }

    private MercadoPagoProperties getMercadoPagoProperties() {
        final var aProperties = new MercadoPagoProperties();
        aProperties.setAccessToken("anAccessToken");
        aProperties.setRequestTimeout(1000);
        aProperties.setConnectionTimeout(1000);
        return aProperties;
    }

    private Customer getCustomer() throws MPJsonParseException {
        return Serializer.deserializeFromJson(Customer.class, "{\"id\":\"1819843505-k3xNlFVH07fXr5\",\"email\":\"kaua.testessss.3@gmail.com\",\"first_name\":\"Kauã\",\"last_name\":\"Santos\",\"phone\":{\"area_code\":\"51\",\"number\":\"999999999\"},\"identification\":{\"type\":\"CPF\",\"number\":\"12345678909\"},\"address\":{\"id\":\"1379645183\",\"zip_code\":\"94850000\",\"street_name\":\"Tiradentes\",\"street_number\":275},\"date_registered\":null,\"description\":null,\"date_created\":\"2024-05-20T12:00:45.909-04:00\",\"date_last_updated\":\"2024-05-20T12:41:01.651-04:00\",\"metadata\":{\"source_sync\":\"source_k\"},\"default_card\":\"1716223230277\",\"default_address\":\"1379645183\",\"cards\":[{\"cardholder\":{\"name\":\"APRO\",\"identification\":{\"number\":null,\"type\":\"CPF\"}},\"customer_id\":\"1819843505-k3xNlFVH07fXr5\",\"date_created\":\"2024-05-20T11:55:24.318-04:00\",\"date_last_updated\":\"2024-05-20T11:55:24.318-04:00\",\"expiration_month\":11,\"expiration_year\":2025,\"first_six_digits\":\"503143\",\"id\":\"1716220894224\",\"issuer\":{\"id\":24,\"name\":\"Mastercard\"},\"last_four_digits\":\"6351\",\"payment_method\":{\"id\":\"master\",\"name\":\"master\",\"payment_type_id\":\"credit_card\",\"thumbnail\":\"http://img.mlstatic.com/org-img/MP3/API/logos/master.gif\",\"secure_thumbnail\":\"https://www.mercadopago.com/org-img/MP3/API/logos/master.gif\"},\"security_code\":{\"length\":3,\"card_location\":\"back\"},\"user_id\":\"1819843505\"},{\"cardholder\":{\"name\":\"APRO\",\"identification\":{\"number\":null,\"type\":\"CPF\"}},\"customer_id\":\"1819843505-k3xNlFVH07fXr5\",\"date_created\":\"2024-05-20T12:39:36.257-04:00\",\"date_last_updated\":\"2024-05-20T12:39:36.257-04:00\",\"expiration_month\":11,\"expiration_year\":2025,\"first_six_digits\":\"423564\",\"id\":\"1716223230277\",\"issuer\":{\"id\":25,\"name\":\"Visa\"},\"last_four_digits\":\"5682\",\"payment_method\":{\"id\":\"visa\",\"name\":\"visa\",\"payment_type_id\":\"credit_card\",\"thumbnail\":\"http://img.mlstatic.com/org-img/MP3/API/logos/visa.gif\",\"secure_thumbnail\":\"https://www.mercadopago.com/org-img/MP3/API/logos/visa.gif\"},\"security_code\":{\"length\":3,\"card_location\":\"back\"},\"user_id\":\"1819843505\"}],\"addresses\":[{\"apartment\":null,\"city\":{\"id\":\"TUxCQ0FMVjUxYzc0\",\"name\":\"Alvorada\"},\"comments\":null,\"country\":{\"id\":\"BR\",\"name\":\"Brasil\"},\"date_created\":\"2024-05-20T12:00:45.831-04:00\",\"date_last_updated\":null,\"floor\":null,\"id\":\"1379645183\",\"municipality\":{\"id\":null,\"name\":null},\"name\":null,\"neighborhood\":{\"id\":null,\"name\":\"Aparecida\"},\"normalized\":true,\"phone\":\"0000000000\",\"state\":{\"id\":\"BR-RS\",\"name\":\"RioGrandedoSul\"},\"street_name\":\"Tiradentes\",\"street_number\":275,\"verifications\":{\"shipment\":{\"errors\":[],\"success\":true}},\"zip_code\":\"94850000\"}],\"live_mode\":false}");
    }
}

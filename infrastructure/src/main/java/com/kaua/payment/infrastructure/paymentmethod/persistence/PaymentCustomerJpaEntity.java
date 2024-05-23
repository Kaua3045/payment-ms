package com.kaua.payment.infrastructure.paymentmethod.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "payments_customers")
public class PaymentCustomerJpaEntity {

    @Id
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "payment_gateway_customer_id", nullable = false, unique = true)
    private String paymentGatewayCustomerId;

    public PaymentCustomerJpaEntity() {
    }

    private PaymentCustomerJpaEntity(
            final String id,
            final String email,
            final String accountId,
            final String paymentGatewayCustomerId
    ) {
        this.id = id;
        this.email = email;
        this.accountId = accountId;
        this.paymentGatewayCustomerId = paymentGatewayCustomerId;
    }

    public static PaymentCustomerJpaEntity create(
            final String email,
            final String accountId,
            final String paymentGatewayCustomerId
    ) {
        return new PaymentCustomerJpaEntity(UUID.randomUUID()
                .toString().replace("-", "")
                .toLowerCase(), email, accountId, paymentGatewayCustomerId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPaymentGatewayCustomerId() {
        return paymentGatewayCustomerId;
    }

    public void setPaymentGatewayCustomerId(String paymentGatewayCustomerId) {
        this.paymentGatewayCustomerId = paymentGatewayCustomerId;
    }

    @Override
    public String toString() {
        return "PaymentCustomerJpaEntity(" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", accountId='" + accountId + '\'' +
                ", paymentGatewayCustomerId='" + paymentGatewayCustomerId + '\'' +
                ')';
    }
}

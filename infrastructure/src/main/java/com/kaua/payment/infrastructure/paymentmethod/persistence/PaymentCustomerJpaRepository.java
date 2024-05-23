package com.kaua.payment.infrastructure.paymentmethod.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCustomerJpaRepository extends JpaRepository<PaymentCustomerJpaEntity, String> {


}

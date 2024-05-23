package com.kaua.payment.infrastructure.exceptions;

import com.kaua.payment.domain.exception.NoStackTraceException;

public class PaymentGatewayException extends NoStackTraceException {

    public PaymentGatewayException(String message, Throwable cause) {
        super(message, cause);
    }
}

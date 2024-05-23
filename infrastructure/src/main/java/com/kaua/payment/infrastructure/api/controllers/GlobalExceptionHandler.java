package com.kaua.payment.infrastructure.api.controllers;

import com.kaua.payment.domain.exception.DomainException;
import com.kaua.payment.infrastructure.exceptions.PaymentGatewayException;
import com.kaua.payment.infrastructure.utils.ApiError;
import com.mercadopago.exceptions.MPApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(final DomainException e) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(e));
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<ApiError> handlePaymentGatewayException(final PaymentGatewayException e) {
        if (e.getCause() instanceof MPApiException apiException) {
            log.info("An error occurred in the payment gateway with this content {}",
                    apiException.getApiResponse().getContent(), apiException);
            return ResponseEntity.badRequest().body(ApiError.from(apiException.getApiResponse().getContent()));
        }
        log.error("An internal server error occurred in the payment gateway", e);
        return ResponseEntity.internalServerError().body(ApiError.from("Internal server error"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(final Exception e) {
        log.error("An unexpected error occurred", e);
        return ResponseEntity.internalServerError().body(ApiError.from("Internal server error"));
    }
}

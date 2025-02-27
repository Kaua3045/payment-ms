package com.kaua.payment.domain.validation.handler;

import com.kaua.payment.domain.exception.DomainException;
import com.kaua.payment.domain.validation.Error;
import com.kaua.payment.domain.validation.Validation;
import com.kaua.payment.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler validationHandler) {
        throw DomainException.with(validationHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}

package com.kaua.payment.domain.exception;

import com.kaua.payment.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final transient List<Error> errors;

    protected DomainException(final List<Error> aErrors) {
        super("DomainException");
        this.errors = aErrors;
    }

    public DomainException(final String aMessage,final List<Error> aErrors) {
        super(aMessage);
        this.errors = aErrors;
    }

    public static DomainException with(final Error aError) {
        return new DomainException(List.of(aError));
    }

    public static DomainException with(final List<Error> aErrors) {
        return new DomainException(aErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}

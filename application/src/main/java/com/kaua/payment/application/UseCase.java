package com.kaua.payment.application;

public abstract class UseCase<OUT, IN> {

    public abstract OUT execute(IN input);
}

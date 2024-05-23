package com.kaua.payment.domain;

import net.datafaker.Faker;

public final class Fixture {

    private static final Faker faker = new Faker();

    private Fixture() {}

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }
}

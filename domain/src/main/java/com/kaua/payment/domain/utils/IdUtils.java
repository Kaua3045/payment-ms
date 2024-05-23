package com.kaua.payment.domain.utils;

import java.util.UUID;

public final class IdUtils {

    // In future change to use a id with timestamp to sort by time

    private IdUtils() {}

    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static String generateIdWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
}

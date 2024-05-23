package com.kaua.payment.domain.utils;

import com.kaua.payment.domain.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InstantUtilsTest extends UnitTest {

    @Test
    void testCallInstantUtilsNow() {
        final var aInstantNow = InstantUtils.now();

        Assertions.assertNotNull(aInstantNow);
    }
}

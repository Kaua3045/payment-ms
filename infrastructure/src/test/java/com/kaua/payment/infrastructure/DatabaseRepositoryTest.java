package com.kaua.payment.infrastructure;

import com.kaua.payment.config.JpaCleanUpExtension;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@ComponentScan(
        basePackages = "com.kaua.payment",
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*RepositoryImpl")
        }
)
@DataJpaTest
@ExtendWith(JpaCleanUpExtension.class)
@Tag("integrationTest")
public @interface DatabaseRepositoryTest {
}

package com.pratap.enterprise.paymentservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class PaymentServiceApplicationTests {

    @Test
    void applicationClassShouldBeInstantiable() {
        PaymentServiceApplication application =
                new PaymentServiceApplication();

        assertNotNull(application);
    }

    @Test
    void mainShouldStartSpringApplication() {
        String[] args = {
                "--spring.main.web-application-type=none"
        };

        try (
                MockedStatic<SpringApplication> springApplication =
                        mockStatic(SpringApplication.class)
        ) {
            springApplication
                    .when(() -> SpringApplication.run(
                            PaymentServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            PaymentServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            PaymentServiceApplication.class,
                            args
                    )
            );
        }
    }
}

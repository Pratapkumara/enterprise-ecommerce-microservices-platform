package com.pratap.enterprise.orderservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class OrderServiceApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        OrderServiceApplication application =
                new OrderServiceApplication();

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
                            OrderServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            OrderServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            OrderServiceApplication.class,
                            args
                    )
            );
        }
    }
}

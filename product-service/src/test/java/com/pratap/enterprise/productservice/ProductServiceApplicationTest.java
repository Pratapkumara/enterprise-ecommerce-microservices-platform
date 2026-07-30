package com.pratap.enterprise.productservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class ProductServiceApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        ProductServiceApplication application =
                new ProductServiceApplication();

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
                            ProductServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            ProductServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            ProductServiceApplication.class,
                            args
                    )
            );
        }
    }
}

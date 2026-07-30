package com.pratap.enterprise.inventoryservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class InventoryServiceApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        InventoryServiceApplication application =
                new InventoryServiceApplication();

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
                            InventoryServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            InventoryServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            InventoryServiceApplication.class,
                            args
                    )
            );
        }
    }
}

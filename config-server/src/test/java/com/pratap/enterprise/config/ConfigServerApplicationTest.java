package com.pratap.enterprise.config;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class ConfigServerApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        ConfigServerApplication application =
                new ConfigServerApplication();

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
                            ConfigServerApplication.class,
                            args
                    ))
                    .thenReturn(null);

            ConfigServerApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            ConfigServerApplication.class,
                            args
                    )
            );
        }
    }
}

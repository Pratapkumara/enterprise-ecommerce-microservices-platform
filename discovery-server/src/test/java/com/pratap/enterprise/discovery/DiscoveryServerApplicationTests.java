package com.pratap.enterprise.discovery;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class DiscoveryServerApplicationTests {

    @Test
    void applicationClassShouldBeInstantiable() {
        DiscoveryServerApplication application =
                new DiscoveryServerApplication();

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
                            DiscoveryServerApplication.class,
                            args
                    ))
                    .thenReturn(null);

            DiscoveryServerApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            DiscoveryServerApplication.class,
                            args
                    )
            );
        }
    }
}

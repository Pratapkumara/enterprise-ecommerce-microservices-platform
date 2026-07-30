package com.pratap.enterprise.notificationservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class NotificationServiceApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        NotificationServiceApplication application =
                new NotificationServiceApplication();

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
                            NotificationServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            NotificationServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            NotificationServiceApplication.class,
                            args
                    )
            );
        }
    }
}

package com.pratap.enterprise.userservice;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class UserServiceApplicationTest {

    @Test
    void applicationClassShouldBeInstantiable() {
        UserServiceApplication application =
                new UserServiceApplication();

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
                            UserServiceApplication.class,
                            args
                    ))
                    .thenReturn(null);

            UserServiceApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            UserServiceApplication.class,
                            args
                    )
            );
        }
    }
}

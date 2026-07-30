package com.pratap.enterprise.api_gateway;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull(this);
    }

    @Test
    void applicationClassShouldBeInstantiable() {
        ApiGatewayApplication application =
                new ApiGatewayApplication();

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
                            ApiGatewayApplication.class,
                            args
                    ))
                    .thenReturn(null);

            ApiGatewayApplication.main(args);

            springApplication.verify(
                    () -> SpringApplication.run(
                            ApiGatewayApplication.class,
                            args
                    )
            );
        }
    }
}

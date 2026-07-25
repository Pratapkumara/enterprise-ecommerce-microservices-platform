package com.pratap.enterprise.userservice.config;

import com.pratap.enterprise.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void applicationContextShouldLoad() {
        userRepository.count();
    }

    @Test
    void publicHealthEndpointShouldBeAccessibleWithoutAuthentication()
            throws Exception {

        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service")
                        .value("USER-SERVICE"));
    }

    @Test
    void protectedUsersEndpointShouldRejectAnonymousRequest()
            throws Exception {

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isForbidden());
    }
}

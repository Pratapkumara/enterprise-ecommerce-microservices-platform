package com.pratap.enterprise.userservice.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY="
        );

        ReflectionTestUtils.setField(
                jwtService,
                "jwtExpiration",
                3600000L
        );

        userDetails = User.builder()
                .username("pratap@example.com")
                .password("encoded-password")
                .roles("USER")
                .build();
    }

    @Test
    void generateTokenShouldCreateTokenWithUsername() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token))
                .isEqualTo("pratap@example.com");
    }

    @Test
    void generateTokenShouldIncludeExtraClaims() {
        String token = jwtService.generateToken(
                Map.of("role", "ADMIN"),
                userDetails
        );

        String role = jwtService.extractClaim(
                token,
                claims -> claims.get("role", String.class)
        );

        assertThat(role).isEqualTo("ADMIN");
    }

    @Test
    void isTokenValidShouldReturnTrueForMatchingUser() {
        String token = jwtService.generateToken(userDetails);

        assertThat(jwtService.isTokenValid(token, userDetails))
                .isTrue();
    }

    @Test
    void isTokenValidShouldReturnFalseForDifferentUser() {
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUser = User.builder()
                .username("other@example.com")
                .password("encoded-password")
                .roles("USER")
                .build();

        assertThat(jwtService.isTokenValid(token, differentUser))
                .isFalse();
    }
}

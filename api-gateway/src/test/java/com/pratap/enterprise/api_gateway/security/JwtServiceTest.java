package com.pratap.enterprise.api_gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private SecretKey signingKey;

    @BeforeEach
    void setUp() {
        signingKey = Keys.secretKeyFor(
                SignatureAlgorithm.HS256
        );

        String encodedSecret =
                Encoders.BASE64.encode(signingKey.getEncoded());

        jwtService = new JwtService();

        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                encodedSecret
        );
    }

    @Test
    void extractUsernameShouldReturnTokenSubject() {
        String token = createToken(
                "user@example.com",
                new Date(System.currentTimeMillis() + 60000)
        );

        String username =
                jwtService.extractUsername(token);

        assertEquals("user@example.com", username);
    }

    @Test
    void extractClaimShouldResolveExpiration() {
        Date expiration =
                new Date(System.currentTimeMillis() + 60000);

        String token = createToken(
                "user@example.com",
                expiration
        );

        Date extracted =
                jwtService.extractClaim(
                        token,
                        claims -> claims.getExpiration()
                );

        assertEquals(
                expiration.getTime() / 1000,
                extracted.getTime() / 1000
        );
    }

    @Test
    void isTokenValidShouldReturnTrueForValidToken() {
        String token = createToken(
                "user@example.com",
                new Date(System.currentTimeMillis() + 60000)
        );

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValidShouldReturnFalseForExpiredToken() {
        String token = createToken(
                "user@example.com",
                new Date(System.currentTimeMillis() - 60000)
        );

        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValidShouldReturnFalseWhenSubjectIsMissing() {
        String token = Jwts.builder()
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + 60000
                        )
                )
                .signWith(signingKey)
                .compact();

        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValidShouldReturnFalseForMalformedToken() {
        assertFalse(
                jwtService.isTokenValid(
                        "not-a-valid-jwt-token"
                )
        );
    }

    private String createToken(
            String subject,
            Date expiration) {

        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(expiration)
                .signWith(signingKey)
                .compact();
    }
}

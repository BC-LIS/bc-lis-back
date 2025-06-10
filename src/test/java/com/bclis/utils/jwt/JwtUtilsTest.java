package com.bclis.utils.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // 32 bytes (256 bits), base64: "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBrZXkgZm9yIHRlc3Rz"
    private final String SECRET_KEY = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBrZXkgZm9yIHRlc3Rz";

    private final String EXPIRATION_TIME = "3600000"; // 1 hora

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        jwtUtils.setSecretKey(SECRET_KEY);
        jwtUtils.setTimeExpiration(EXPIRATION_TIME);
    }

    @Test
    void testGenerateAndValidateToken() {
        // Setup usuario simulado
        GrantedAuthority authority = () -> "ROLE_USER";
        var auth = new UsernamePasswordAuthenticationToken("testuser", null, List.of(authority));

        // Generar token
        String token = jwtUtils.generateAccessToken(auth);
        assertNotNull(token);

        // Validar token
        boolean isValid = jwtUtils.isTokenValid(token);
        assertTrue(isValid);

        // Extraer username
        String username = jwtUtils.getUsernameFromToken(token);
        assertEquals("testuser", username);

        // Extraer claim específico
        String authorities = jwtUtils.getClaim(token, "authorities");
        assertEquals("ROLE_USER", authorities);
    }

    @Test
    void testInvalidTokenReturnsFalse() {
        String invalidToken = "malformed.token.value";

        boolean isValid = jwtUtils.isTokenValid(invalidToken);
        assertFalse(isValid);
    }

    @Test
    void testGetUsernameFromSecurityContextAuthenticated() {
        // Simula autenticación en SecurityContext
        var auth = new UsernamePasswordAuthenticationToken("mockUser", null, List.of(() -> "ROLE_USER"));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        String username = jwtUtils.getUsernameFromSecurityContext();
        assertEquals("mockUser", username);
    }

    @Test
    void testGetUsernameFromSecurityContextUnauthenticated() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();

        String username = jwtUtils.getUsernameFromSecurityContext();
        assertEquals("", username);
    }

    @Test
    void testGetAuthoritiesFromSecurityContext() {
        var authorities = List.of(
                (GrantedAuthority) () -> "ROLE_USER",
                (GrantedAuthority) () -> "ROLE_ADMIN"
        );
        var auth = new UsernamePasswordAuthenticationToken("user", null, authorities);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        List<String> result = jwtUtils.getAuthoritiesSecurityContext();
        assertEquals(2, result.size());
        assertTrue(result.contains("ROLE_USER"));
        assertTrue(result.contains("ROLE_ADMIN"));
    }

    @Test
    void testGetAuthoritiesEmptyWhenUnauthenticated() {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();

        List<String> result = jwtUtils.getAuthoritiesSecurityContext();
        assertTrue(result.isEmpty());
    }
}

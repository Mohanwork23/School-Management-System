package com.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private static final String SECRET = "test-secret-that-is-at-least-thirty-two-bytes-long";

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3_600_000L);
        jwtUtil.init();
        userDetails = new User("STU001", "password", List.of());
    }

    @Test
    void generatesAndValidatesTokenForMatchingUser() {
        String token = jwtUtil.generateToken(userDetails);

        assertEquals("STU001", jwtUtil.extractUsername(token));
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void rejectsTokenForDifferentUser() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails anotherUser = new User("STU002", "password", List.of());

        assertFalse(jwtUtil.validateToken(token, anotherUser));
    }

    @Test
    void rejectsWeakSecret() {
        JwtUtil weakSecretJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(weakSecretJwtUtil, "secret", "too-short");

        assertThrows(IllegalStateException.class, weakSecretJwtUtil::init);
    }
}

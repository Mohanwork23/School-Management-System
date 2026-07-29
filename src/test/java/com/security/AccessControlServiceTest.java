package com.security;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

class AccessControlServiceTest {

    private final AccessControlService accessControlService = new AccessControlService();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void permitsAccessToTheAuthenticatedUsersOwnResource() {
        authenticateAs("STU001");

        assertDoesNotThrow(() -> accessControlService.requireCurrentUser("STU001"));
    }

    @Test
    void rejectsAccessToAnotherUsersResource() {
        authenticateAs("STU001");

        assertThrows(AccessDeniedException.class,
                () -> accessControlService.requireCurrentUser("STU002"));
    }

    private void authenticateAs(String username) {
        User principal = new User(username, "password", List.of());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));
    }
}

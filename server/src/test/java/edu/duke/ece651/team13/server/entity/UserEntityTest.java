package edu.duke.ece651.team13.server.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserEntityTest {

    private final String testName = "testUser";
    private final String testEmail = "testUser@gmail.com";
    private final String testPassword = "testPW";
    private UserEntity user; //UserEntity under test

    @BeforeEach
    void setUp() {
        user = new UserEntity(0L, testName, testEmail, testPassword);
    }

    @Test
    void test_getId() {
        assertEquals(0L, user.getId());
    }

    @Test
    void test_getFullName() {
        assertEquals(testName, user.getFullName());
    }

    @Test
    void test_getEmail() {
        assertEquals(testEmail, user.getEmail());
    }

    @Test
    void test_getPassword() {
        assertEquals(testPassword, user.getPassword());
    }

    @Test
    void test_setId() {
        user.setId(2L);
        assertEquals(2L, user.getId());
    }

    @Test
    void test_setFullName() {
        user.setFullName("anotherName");
        assertEquals("anotherName", user.getFullName());
    }

    @Test
    void test_setEmail() {
        user.setEmail("anotherEmail@gmail.com");
        assertEquals("anotherEmail@gmail.com", user.getEmail());
    }

    @Test
    void test_setPassword() {
        user.setPassword("anotherPassword");
        assertEquals("anotherPassword", user.getPassword());
    }

    @Test
    void test_springSecurityPlaceholderMethods() {
        assertNull(user.getAuthorities());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }
}
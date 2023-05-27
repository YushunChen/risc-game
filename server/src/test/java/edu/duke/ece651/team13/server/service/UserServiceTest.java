package edu.duke.ece651.team13.server.service;

import edu.duke.ece651.team13.server.auth.RegisterRequest;
import edu.duke.ece651.team13.server.entity.UserEntity;
import edu.duke.ece651.team13.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static edu.duke.ece651.team13.server.MockDataUtil.getUserEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserServiceImpl service; //Service under test

    @Mock
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository);
    }

    @Test
    void test_createUser() {
        UserEntity user = getUserEntity();
        when(repository.save(any(UserEntity.class))).thenReturn(user);
        UserEntity actual = service.createUser("user", "user@gmail.com", "123456");
        assertEquals(user, actual);
        verify(repository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test_getUserById() {
        UserEntity user = getUserEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.findById(2L)).thenReturn(Optional.empty());

        UserEntity actual = service.getUserById(1L);
        assertEquals(user, actual);
        verify(repository, times(1)).findById(1L);
        verifyNoMoreInteractions(repository);

        assertThrows(NoSuchElementException.class, () -> service.getUserById(2L));
    }

    @Test
    void test_getUserByEmail() {
        UserEntity user = getUserEntity();
        when(repository.findByEmail("email1@gmail.com")).thenReturn(Optional.of(user));
        when(repository.findByEmail("email2@gmail.com")).thenReturn(Optional.empty());

        UserEntity actual = service.getUserByEmail("email1@gmail.com");
        assertEquals(user, actual);
        verify(repository, times(1)).findByEmail("email1@gmail.com");
        verifyNoMoreInteractions(repository);

        assertThrows(NoSuchElementException.class, () -> service.getUserByEmail("email2@gmail.com"));

    }

    @Test
    void test_isUserPresent() {
        String email1 = "email1@gmail.com";
        String email2 = "email2@gmail.com";
        UserEntity user1 = getUserEntity();
        user1.setEmail(email1);
        UserEntity user2 = getUserEntity();
        user2.setEmail(email2);
        // user1 exists
        when(repository.findByEmail(email1)).thenReturn(Optional.of(user1));
        // user2 doesn't exist
        when(repository.findByEmail(email2)).thenReturn(Optional.empty());

        String expected = "An account with the same email already exists!";
        assertEquals(expected, service.isUserPresent(new RegisterRequest(user1.getFullName(), user1.getEmail(), user1.getPassword())));
        assertNull(service.isUserPresent(new RegisterRequest(user2.getFullName(), user2.getEmail(), user2.getPassword())));

        verify(repository, times(1)).findByEmail(email1);
        verify(repository, times(1)).findByEmail(email2);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test_updateUserPassword() {
        UserEntity user = getUserEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity actual = service.updateUserPassword(1L, "newPassword");
        assertEquals(user, actual);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(UserEntity.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void test_loadUserByUsername() {
        UserEntity user = getUserEntity();
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        service.loadUserByUsername("newPassword");
        verify(repository, times(1)).findByEmail(any());
        verifyNoMoreInteractions(repository);

        when(repository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, ()-> service.loadUserByUsername("newPassword"));

    }
}
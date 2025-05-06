package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterUser_successful() {
        String username = "newuser";
        String email = "user@example.com";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        try {
            userService.registerUser(username, email, password);

            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(captor.capture());
            User saved = captor.getValue();

            System.out.println("✅ Registration succeeded:");
            System.out.println("Username: " + saved.getUsername());
            System.out.println("Email: " + saved.getEmail());
            System.out.println("Hashed Password: " + saved.getPasswordHash());

            assertTrue(saved.getPasswordHash().startsWith("$2a$"));
        } catch (Exception e) {
            System.out.println("❌ Registration failed unexpectedly: " + e.getMessage());
            fail();
        }
    }

    @Test
    void testRegisterUser_usernameTaken() {
        String username = "existing";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        try {
            userService.registerUser(username, "test@example.com", "password");
            System.out.println("❌ Registration should have failed but didn't.");
            fail();
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Correctly failed registration: " + e.getMessage());
            assertEquals("Username already exists", e.getMessage());
        }
    }

    @Test
    void testLoginUser_successful() {
        String username = "loginUser";
        String rawPassword = "securePass";
        String hashed = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(rawPassword);
        User mockUser = new User(1, username, "email@test.com", hashed);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.loginUser(username, rawPassword);
        if (result.isPresent()) {
            System.out.println("✅ Login successful: " + result.get().getUsername());
        } else {
            System.out.println("❌ Login failed unexpectedly.");
            fail();
        }
    }

    @Test
    void testLoginUser_wrongPassword() {
        String username = "loginUser";
        String rawPassword = "wrongPass";
        String hashed = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("correctPass");
        User mockUser = new User(1, username, "email@test.com", hashed);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.loginUser(username, rawPassword);
        if (result.isEmpty()) {
            System.out.println("✅ Login failed correctly for wrong password.");
        } else {
            System.out.println("❌ Login should have failed, but succeeded.");
            fail();
        }
    }
}

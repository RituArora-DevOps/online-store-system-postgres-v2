package com.oss.ossv1.service;

import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String username, String email, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        // Password validation
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        if (!Pattern.matches(passwordRegex, rawPassword)) {
            throw new IllegalArgumentException("Password must be at least 8 characters, include upper/lowercase and a number");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User(null, username, email, hashedPassword);
        userRepository.save(newUser);
    }

    public Optional<User> loginUser(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}

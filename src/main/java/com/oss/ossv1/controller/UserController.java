package com.oss.ossv1.controller;

import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password) {
        try {
            userService.registerUser(username, email, password);
            return "User registered successfully.";
        } catch (IllegalArgumentException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {
        Optional<User> userOpt = userService.loginUser(username, password);
        return userOpt.map(user -> "Login successful: Welcome " + user.getUsername())
                .orElse("Login failed: Invalid credentials.");
    }
}

package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserDataInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void seedUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User(null, "alice", "alice@example.com", passwordEncoder.encode("alice123")),
                    new User(null, "bob", "bob@example.com", passwordEncoder.encode("bob123")),
                    new User(null, "charlie", "charlie@example.com", passwordEncoder.encode("charlie123"))
            );
            userRepository.saveAll(users);
            System.out.println("Seeded users into the database.");
        } else {
            System.out.println(" User table already has data. Skipping user seeding.");
        }
    }
}

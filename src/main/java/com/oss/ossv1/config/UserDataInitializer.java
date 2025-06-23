package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // Check if alice exists and update her admin status
        Optional<User> aliceOpt = userRepository.findByUsername("alice");
        if (aliceOpt.isPresent()) {
            User alice = aliceOpt.get();
            if (!alice.isAdmin()) {
                alice.setAdmin(true);
                userRepository.save(alice);
                System.out.println("Updated alice to admin status");
            }
        } else {
            // Create alice as admin if she doesn't exist
            User alice = new User("alice", "alice@example.com", passwordEncoder.encode("alice123"), true);
            userRepository.save(alice);
            System.out.println("Created alice as admin user");
        }

        if (userRepository.findByUsername("bob").isEmpty()) {
            User bob = new User("bob", "bob@example.com", passwordEncoder.encode("bob123"), false);
            userRepository.save(bob);
            System.out.println("Created bob as regular user");
        }

        if (userRepository.findByUsername("charlie").isEmpty()) {
            User charlie = new User("charlie", "charlie@example.com", passwordEncoder.encode("charlie123"), false);
            userRepository.save(charlie);
            System.out.println("Created charlie as regular user");
        }

        System.out.println("User initialization complete:");
        System.out.println("- alice (admin): alice123");
        System.out.println("- bob (user): bob123");
        System.out.println("- charlie (user): charlie123");
    }
}

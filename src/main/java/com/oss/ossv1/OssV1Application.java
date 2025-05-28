package com.oss.ossv1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;


/**
 * Spring Boot application starter class.
 *
 * This runs your Spring context in the background.
 *
 * Design Pattern: None
 * SOLID: SRP - initializes Spring Boot
 */
@SpringBootApplication
@Validated // To enable validation globally
public class OssV1Application {

    public static void main(String[] args) {
        SpringApplication.run(OssV1Application.class, args);
    }
    // NEXT FLOW: Initializes all @RestController, @Service, @Repository classes
}

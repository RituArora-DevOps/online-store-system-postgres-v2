package com.oss.ossv1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oss.ossv1.behavioral.observer.ReviewNotificationManager;

/**
 * Spring configuration for the Observer pattern implementation.
 * Sets up the ReviewNotificationManager subject for managing review notifications.
 */
@Configuration
public class ReviewConfig {
    
    /**
     * Create a ReviewNotificationManager bean for managing review notifications
     */
    @Bean
    public ReviewNotificationManager reviewNotificationManager() {
        ReviewNotificationManager reviewNotificationManager = new ReviewNotificationManager();
        System.out.println("ReviewNotificationManager bean created for Observer pattern");
        return reviewNotificationManager;
    }
} 
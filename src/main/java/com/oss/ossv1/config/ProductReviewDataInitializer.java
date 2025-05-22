package com.oss.ossv1.config;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.data.entity.User;
import com.oss.ossv1.data.repository.ProductRepository;
import com.oss.ossv1.data.repository.ReviewRepository;
import com.oss.ossv1.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class ProductReviewDataInitializer {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void seedReviews() {
        if (reviewRepository.count() > 0) {
            System.out.println(" Product reviews already seeded. Skipping...");
            return;
        }

        List<User> users = userRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (users.isEmpty() || products.isEmpty()) {
            System.out.println(" Cannot seed reviews: Users or Products are missing.");
            return;
        }

        Random rand = new Random();
        String[] comments = {
                "Excellent quality!", "Very satisfied.", "Could be better.",
                "Not worth the price.", "Would buy again.", "Fast delivery!",
                "Poor packaging.", "Highly recommended.", "Decent value.", "Amazing product!"
        };

        IntStream.range(0, 100).forEach(i -> {
            ProductReview review = new ProductReview();
            review.setProduct(products.get(rand.nextInt(products.size())));
            review.setUser(users.get(rand.nextInt(users.size())));
            review.setRating(rand.nextInt(5) + 1);
            review.setComment(comments[rand.nextInt(comments.length)]);
            review.setReviewDate(LocalDateTime.now().minusDays(rand.nextInt(60)));
            reviewRepository.save(review);
        });

        System.out.println(" Seeded 100 product reviews.");
    }
}

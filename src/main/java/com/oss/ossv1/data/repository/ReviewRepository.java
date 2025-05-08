package com.oss.ossv1.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oss.ossv1.data.entity.Product;
import com.oss.ossv1.data.entity.ProductReview;
import com.oss.ossv1.data.entity.User;

/**
 * Repository interface for ProductReview entity
 */
@Repository
public interface ReviewRepository extends JpaRepository<ProductReview, Integer> {
    
    /**
     * Find reviews by product
     * @param product the product to find reviews for
     * @return list of reviews for the product
     */
    List<ProductReview> findByProduct(Product product);
    
    /**
     * Find reviews by user
     * @param user the user to find reviews for
     * @return list of reviews by the user
     */
    List<ProductReview> findByUser(User user);
}

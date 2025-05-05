package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity // Declares that this Java class is a JPA entity, meaning it maps to a table in your database.
@Table(name = "product_review") //
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the reviewed product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rating", nullable = false)
    private int rating; // 1 to 5

    @Column(name = "comment", length = 255)
    private String comment;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;
}

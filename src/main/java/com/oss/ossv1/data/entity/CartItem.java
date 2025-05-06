/**
 *

package com.oss.ossv1.data.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CartItem")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relationship with product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    // Many cart items belong to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
 */
package com.oss.ossv1.data.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // corrected
    //@Column(name = "user_id", nullable = false)
    // replaced with
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    //private Integer userId; // This tells JPA that this is a foreign key, this entity has a column called user-id, but dont worry about what is connects to, jsut treat it as an integer
    // Drawbacks: No FK constraint created, No way to access teh full usert abject via JPA (e.g. cart.getUser().getUsername() won't work) and hibernate cannot perform joins or lazy loading between Cart and User
    private User user; // This helps in ORM and thus Java handles joins automatically

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
} 